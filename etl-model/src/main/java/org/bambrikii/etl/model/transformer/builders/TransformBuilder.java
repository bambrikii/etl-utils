package org.bambrikii.etl.model.transformer.builders;

import org.bambrikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambrikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambrikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambrikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambrikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambrikii.etl.model.transformer.adapters.EtlModelWriter;
import org.bambrikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambrikii.etl.model.transformer.mapping.model.MappingRoot;
import org.bambrikii.etl.model.transformer.schema.model.SchemaModel;
import org.bambrikii.etl.model.transformer.schema.model.SchemaField;
import org.bambrikii.etl.model.transformer.schema.model.SchemaRoot;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.bambrikii.etl.model.transformer.builders.EtlAdapterUtils.createFieldReader;
import static org.bambrikii.etl.model.transformer.builders.EtlAdapterUtils.createFieldWriter;

public class TransformBuilder {
    private SchemaRoot schemaRoot;
    private MappingRoot mappingRoot;

    private EtlModelReader modelReader;
    private EtlModelWriter modelWriter;

    private EtlFieldReader fieldReader;
    private EtlFieldWriter fieldWriter;

    private String modelReaderType;
    private String modelWriterType;
    private List<EtlFieldConversionPair> fieldMaps = new ArrayList<>();

    public static TransformBuilder of(
            EtlModelReader<?> reader, EtlModelWriter<?> writer,
            SchemaRoot models, MappingRoot mappings
    ) {
        return new TransformBuilder()
                .modelReader(reader)
                .modelWriter(writer)
                .models(models)
                .mappings(mappings);
    }

    public TransformBuilder modelReader(EtlModelReader modelReader) {
        this.modelReaderType = validateName(modelReader);
        this.modelReader = modelReader;
        this.fieldReader = modelReader.createFieldReader();
        return this;
    }

    private String validateName(Object strategy) {
        if (!(strategy instanceof EtlNamable)) {
            throw new EtlRuntimeException("Expecting " + EtlNamable.class.getName() + " to be implemented for the strategy " + strategy.getClass().getName() + "!");
        }
        return ((EtlNamable) strategy).getName();
    }


    public TransformBuilder modelWriter(EtlModelWriter modelWriter) {
        this.modelWriterType = validateName(modelWriter);
        this.modelWriter = modelWriter;
        this.fieldWriter = modelWriter.createFieldWriter();
        return this;
    }

    public TransformBuilder fieldMap(String from, String to, String fromType, String toType) {
        fieldMaps.add(new EtlFieldConversionPair(
                fieldReader.createOne(from, fromType),
                fieldWriter.createOne(to, toType)
        ));
        return this;
    }

    public TransformBuilder fieldMapString(String from, String to) {
        return fieldMap(from, to, EtlFieldReader.STRING, EtlFieldReader.STRING);
    }

    public TransformBuilder fieldMapInt(String from, String to) {
        return fieldMap(from, to, EtlFieldReader.INT, EtlFieldReader.INT);
    }

    public TransformBuilder fieldMapDouble(String from, String to) {
        return fieldMap(from, to, EtlFieldReader.DOUBLE, EtlFieldReader.DOUBLE);
    }

    public <I> void transform() {
        EtlModelAdapter adapter = new EtlModelAdapter(fieldMaps);
        adapter.adapt(modelReader, modelWriter);
    }

    public TransformBuilder models(SchemaRoot modelRootConfig) {
        this.schemaRoot = modelRootConfig;
        return this;
    }

    public TransformBuilder mappings(MappingRoot mappingRoot) {
        this.mappingRoot = mappingRoot;
        return this;
    }

    public TransformBuilder mapping(String conversionName) {
        mappingRoot
                .getMappingModels()
                .stream()
                .filter(converter -> converter.getName().equals(conversionName))
                .findFirst()
                .ifPresent(conversion -> {
                    String sourceReaderType = conversion.getSourceReaderType();
                    if (!modelReaderType.equals(sourceReaderType)) {
                        throw new RuntimeException("Source conversion strategy [" + sourceReaderType + "] not found. Available strategies: [" + modelReaderType + "]");
                    }
                    String targetReaderType = conversion.getTargetReaderType();
                    if (!modelWriterType.equals(targetReaderType)) {
                        throw new RuntimeException("Target conversion strategy [" + targetReaderType + "] not found. Available strategies: [" + modelWriterType + "]");
                    }
                    LinkedHashMap<String, SchemaField> sourceModelFieldConfig = extractSourceModelConfig(conversion.getSourceSchema(), conversion.getSourceModel());
                    LinkedHashMap<String, SchemaField> targetModelFieldConfig = extractTargetModelConfig(conversion.getTargetSchema(), conversion.getTargetModel());
                    conversion
                            .getFields()
                            .forEach(fieldConversionConfig -> {
                                EtlFieldExtractable fieldReader = createFieldReader(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, this.fieldReader);
                                EtlFieldLoadable fieldWriter = createFieldWriter(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, this.fieldWriter);
                                fieldMaps.add(new EtlFieldConversionPair(fieldReader, fieldWriter));
                            });
                });
        return this;
    }

    private LinkedHashMap<String, SchemaField> extractModel(String schemaName, String modelName) {
        SchemaModel modelConfig = schemaRoot
                .getSchemas()
                .stream()
                .filter(schema -> schema.getName().equals(schemaName))
                .findFirst()
                .get()
                .getModels()
                .stream()
                .filter(converter -> converter.getName().equals(modelName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Model " + modelName + " has not been found!"));
        return modelConfig
                .getFields()
                .stream()
                .collect(Collectors.toMap(SchemaField::getName, Function.identity(), (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                }, LinkedHashMap::new));
    }

    private LinkedHashMap<String, SchemaField> extractSourceModelConfig(String schemaName, String modelName) {
        return extractModel(schemaName, modelName);
    }

    private LinkedHashMap<String, SchemaField> extractTargetModelConfig(String schemaName, String modelName) {
        if (schemaName == null) {
            return null;
        }
        if (modelName == null) {
            return null;
        }
        return extractSourceModelConfig(schemaName, modelName);
    }
}
