package org.bambikii.etl.model.transformer.builders;

import jakarta.xml.bind.JAXBException;
import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;
import org.bambikii.etl.model.transformer.config.EtlConfigMarshaller;
import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelFieldConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;

import java.io.InputStream;
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

    protected ConversionRootConfig conversionRoot;
    protected Map<String, LinkedHashMap<String, ModelFieldConfig>> modelFieldConfigs;

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

    public EtlAdapterConfigBuilder modelConfig(InputStream modelInputStream) throws JAXBException {
        ModelRootConfig modelRoot = EtlConfigMarshaller.unmarshalModelConfig(modelInputStream);

        Map<String, LinkedHashMap<String, ModelFieldConfig>> modelFieldConfigs = new HashMap<>();
        modelRoot
                .getModelConfigs()
                .forEach(modelConfig -> {
                    LinkedHashMap<String, ModelFieldConfig> fieldConfigs = new LinkedHashMap<>();
                    modelFieldConfigs.put(modelConfig.getName(), fieldConfigs);
                    modelConfig
                            .getModelFields()
                            .forEach(fieldConfig -> fieldConfigs.put(fieldConfig.getName(), fieldConfig));
                });
        this.modelFieldConfigs = modelFieldConfigs;
        return this;
    }

    public EtlAdapterConfigBuilder conversionConfig(InputStream conversionInputStream) throws JAXBException {
        conversionRoot = EtlConfigMarshaller.unmarshalConversionConfig(conversionInputStream);
        return this;
    }

    public Map<String, EtlModelAdapter> buildMap() {
        Map<String, EtlModelAdapter> converters = new HashMap<>();
        conversionRoot
                .getModelConversionConfigs()
                .forEach(conversion -> {
                    LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig = modelFieldConfigs.get(conversion.getSourceModel());
                    LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig = modelFieldConfigs.get(conversion.getTargetModel());
                    String sourceReaderType = conversion.getSourceReaderType();
                    if (!readerStrategies.containsKey(sourceReaderType)) {
                        throw new RuntimeException("Source conversion strategy [" + sourceReaderType + "] not found. Available strategies: [" + readerStrategies.keySet() + "]");
                    }
                    EtlFieldReader etlFieldReaderStrategy = readerStrategies.get(sourceReaderType);
                    String targetReaderType = conversion.getTargetReaderType();
                    if (!readerStrategies.containsKey(sourceReaderType)) {
                        throw new RuntimeException("Target conversion strategy [" + targetReaderType + "] not found. Available strategies: [" + writerStrategies.keySet() + "]");
                    }
                    EtlFieldWriter etlFieldWriterStrategy = writerStrategies.get(targetReaderType);
                    List<EtlFieldConversionPair> etlFieldConversionPairs = new ArrayList<>();
                    conversion
                            .getFields()
                            .forEach(fieldConversionConfig -> {
                                EtlFieldExtractable fieldReader = createFieldReader(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, etlFieldReaderStrategy);
                                EtlFieldLoadable fieldWriter = createFieldWriter(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, etlFieldWriterStrategy);
                                etlFieldConversionPairs.add(new EtlFieldConversionPair(fieldReader, fieldWriter));
                            });
                    converters.put(conversion.getName(), new EtlModelAdapter(etlFieldConversionPairs));
                });
        return converters;
    }
}
