package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambikii.etl.model.transformer.adapters.EtlModelWriter;
import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelConfig;
import org.bambikii.etl.model.transformer.config.model.ModelFieldConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.bambikii.etl.model.transformer.builders.EtlAdapterUtils.createFieldReader;
import static org.bambikii.etl.model.transformer.builders.EtlAdapterUtils.createFieldWriter;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.DOUBLE;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.INT;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.STRING;

public class TransformBuilder {
    private EtlFieldReader fieldReader;
    private EtlFieldWriter fieldWriter;
    private List<EtlFieldConversionPair> fieldMaps = new ArrayList<>();
    private EtlModelReader modelReader;
    private EtlModelWriter modelWriter;
    private ConversionRootConfig conversionRootConfig;
    private ModelRootConfig modelRootConfig;
    private String modelReaderType;
    private String modelWriterType;

    public static TransformBuilder of(
            EtlModelReader<?> reader, EtlModelWriter<?> writer,
            ModelRootConfig models, ConversionRootConfig conversions
    ) {
        return new TransformBuilder()
                .modelReader(reader)
                .modelWriter(writer)
                .models(models)
                .conversions(conversions);
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
        return fieldMap(from, to, STRING, STRING);
    }

    public TransformBuilder fieldMapInt(String from, String to) {
        return fieldMap(from, to, INT, INT);
    }

    public TransformBuilder fieldMapDouble(String from, String to) {
        return fieldMap(from, to, DOUBLE, DOUBLE);
    }

    public <I> void transform() {
        EtlModelAdapter adapter = new EtlModelAdapter(fieldMaps);
        adapter.adapt(modelReader, modelWriter);
    }

    public TransformBuilder models(ModelRootConfig modelRootConfig) {
        this.modelRootConfig = modelRootConfig;
        return this;
    }

    public TransformBuilder conversions(ConversionRootConfig conversionRootConfig) {
        this.conversionRootConfig = conversionRootConfig;
        return this;
    }

    public TransformBuilder fieldsByConversion(String conversionName) {
        conversionRootConfig
                .getModelConversionConfigs()
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
                    LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig = extractSourceModelConfig(conversion.getSourceModel());
                    LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig = extractTargetModelConfig(conversion.getTargetModel());
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

    private LinkedHashMap<String, ModelFieldConfig> extractModel(String modelName) {
        ModelConfig modelConfig = modelRootConfig
                .getModelConfigs()
                .stream()
                .filter(converter -> converter.getName().equals(modelName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Model " + modelName + " has not been found!"));
        return modelConfig
                .getModelFields()
                .stream()
                .collect(Collectors.toMap(ModelFieldConfig::getName, Function.identity(), (u, v) -> {
                    throw new IllegalStateException(String.format("Duplicate key %s", u));
                }, LinkedHashMap::new));
    }

    private LinkedHashMap<String, ModelFieldConfig> extractSourceModelConfig(String modelName) {
        return extractModel(modelName);
    }

    private LinkedHashMap<String, ModelFieldConfig> extractTargetModelConfig(String modelName) {
        if (modelName == null) {
            return null;
        }
        return extractSourceModelConfig(modelName);
    }
}
