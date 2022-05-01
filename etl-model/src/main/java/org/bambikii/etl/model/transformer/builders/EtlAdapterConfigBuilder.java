package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambikii.etl.model.transformer.mapping.model.MappingRoot;
import org.bambikii.etl.model.transformer.schema.model.SchemaField;
import org.bambikii.etl.model.transformer.schema.model.SchemaRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.bambikii.etl.model.transformer.builders.EtlAdapterUtils.createFieldReader;
import static org.bambikii.etl.model.transformer.builders.EtlAdapterUtils.createFieldWriter;

public class EtlAdapterConfigBuilder {
    protected final Map<String, EtlFieldReader> readerStrategies;
    protected final Map<String, EtlFieldWriter> writerStrategies;

    protected MappingRoot mappingRoot;
    protected LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, SchemaField>>> schemaConfigs;

    public EtlAdapterConfigBuilder() {
        this.readerStrategies = new HashMap<>();
        this.writerStrategies = new HashMap<>();
    }

    public EtlAdapterConfigBuilder readerStrategy(String name, EtlFieldReader strategy) {
        readerStrategies.put(name, strategy);
        return this;
    }

    public EtlAdapterConfigBuilder writerStrategy(String name, EtlFieldWriter strategy) {
        writerStrategies.put(name, strategy);
        return this;
    }

    private String validateName(Object strategy) {
        if (!(strategy instanceof EtlNamable)) {
            throw new EtlRuntimeException("Expecting " + EtlNamable.class.getName() + " to be implemented for the strategy " + strategy.getClass().getName() + "!");
        }
        return ((EtlNamable) strategy).getName();
    }

    public EtlAdapterConfigBuilder readerStrategy(EtlFieldReader strategy) {
        String name = validateName(strategy);
        readerStrategies.put(name, strategy);
        return this;
    }

    public EtlAdapterConfigBuilder writerStrategy(EtlFieldWriter strategy) {
        String name = validateName(strategy);
        writerStrategies.put(name, strategy);
        return this;
    }

    public EtlAdapterConfigBuilder modelConfig(SchemaRoot modelRoot) {
        LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, SchemaField>>> schemaConfigs = new LinkedHashMap<>();
        modelRoot
                .getSchemas()
                .forEach(schema -> {
                    String schemaName = schema.getName();
                    if (!schemaConfigs.containsKey(schemaName)) {
                        schemaConfigs.put(schemaName, new LinkedHashMap<>());
                    }
                    LinkedHashMap<String, LinkedHashMap<String, SchemaField>> modelFieldConfigs = schemaConfigs.get(schemaName);
                    schema.getModels()
                            .forEach(modelConfig -> {
                                if (!modelFieldConfigs.containsKey(modelConfig.getName())) {
                                    modelFieldConfigs.put(modelConfig.getName(), new LinkedHashMap<>());
                                }
                                LinkedHashMap<String, SchemaField> fieldConfigs = modelFieldConfigs.get(modelConfig.getName());
                                modelFieldConfigs.put(modelConfig.getName(), fieldConfigs);
                                modelConfig
                                        .getFields()
                                        .forEach(fieldConfig -> fieldConfigs.put(fieldConfig.getName(), fieldConfig));
                            });
                })
        ;
        this.schemaConfigs = schemaConfigs;
        return this;
    }

    public EtlAdapterConfigBuilder mappingConfig(MappingRoot mappingRoot) {
        this.mappingRoot = mappingRoot;
        return this;
    }

    public Map<String, EtlModelAdapter> buildMap() {
        Map<String, EtlModelAdapter> maps = new HashMap<>();
        mappingRoot
                .getMappingModels()
                .forEach(mappingModel -> {
                    LinkedHashMap<String, SchemaField> sourceModelFieldConfig = schemaConfigs.get(mappingModel.getSourceSchema()).get(mappingModel.getSourceModel());
                    LinkedHashMap<String, SchemaField> targetModelFieldConfig = schemaConfigs.get(mappingModel.getTargetSchema()).get(mappingModel.getTargetModel());
                    String sourceReaderType = mappingModel.getSourceReaderType();
                    if (!readerStrategies.containsKey(sourceReaderType)) {
                        throw new RuntimeException("Source mappingModel strategy [" + sourceReaderType + "] not found. Available strategies: [" + readerStrategies.keySet() + "]");
                    }
                    EtlFieldReader etlFieldReaderStrategy = readerStrategies.get(sourceReaderType);
                    String targetReaderType = mappingModel.getTargetReaderType();
                    if (!readerStrategies.containsKey(sourceReaderType)) {
                        throw new RuntimeException("Target mappingModel strategy [" + targetReaderType + "] not found. Available strategies: [" + writerStrategies.keySet() + "]");
                    }
                    EtlFieldWriter etlFieldWriterStrategy = writerStrategies.get(targetReaderType);
                    List<EtlFieldConversionPair> etlFieldConversionPairs = new ArrayList<>();
                    mappingModel
                            .getFields()
                            .forEach(mappingField -> {
                                EtlFieldExtractable fieldReader = createFieldReader(sourceModelFieldConfig, targetModelFieldConfig, mappingField, etlFieldReaderStrategy);
                                EtlFieldLoadable fieldWriter = createFieldWriter(sourceModelFieldConfig, targetModelFieldConfig, mappingField, etlFieldWriterStrategy);
                                etlFieldConversionPairs.add(new EtlFieldConversionPair(fieldReader, fieldWriter));
                            });
                    maps.put(mappingModel.getName(), new EtlModelAdapter(etlFieldConversionPairs));
                });
        return maps;
    }
}
