package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;
import org.bambikii.etl.model.transformer.adapters.FieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelFieldAdapter;
import org.bambikii.etl.model.transformer.config.ConfigMarshaller;
import org.bambikii.etl.model.transformer.config.model.*;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.*;

public class ConverterBuilder {
    private final ConfigMarshaller marshaller;
    private final Map<String, FieldReaderStrategy> readerStrategies;
    private final Map<String, FieldWriterStrategy> writerStrategies;
    private ConversionRootConfig conversionRoot;
    private Map<String, LinkedHashMap<String, ModelFieldConfig>> modelFieldConfigs;

    public ConverterBuilder() {
        this.marshaller = new ConfigMarshaller();
        this.readerStrategies = new HashMap<>();
        this.writerStrategies = new HashMap<>();
    }

    public ConverterBuilder readerStrategy(String name, FieldReaderStrategy strategy) {
        readerStrategies.put(name, strategy);
        return this;
    }

    public ConverterBuilder writerStrategy(String name, FieldWriterStrategy strategy) {
        writerStrategies.put(name, strategy);
        return this;
    }

    public ConverterBuilder modelConfig(InputStream modelInputStream) throws JAXBException {
        ModelRootConfig modelRoot = marshaller.unmarshalModelConfig(modelInputStream);

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

    public ConverterBuilder converterConfig(InputStream conversionInputStream) throws JAXBException {
        conversionRoot = marshaller.unmarshalConversionConfig(conversionInputStream);
        return this;
    }

    public Map<String, ModelFieldAdapter> build() {
        Map<String, ModelFieldAdapter> converters = new HashMap<>();
        conversionRoot
                .getModelConversionConfigs()
                .forEach(conversion -> {
                    String sourceModel = conversion.getSourceModel();
                    String targetModel = conversion.getTargetModel();
                    LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig = modelFieldConfigs.get(sourceModel);
                    LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig = modelFieldConfigs.get(targetModel);
                    FieldReaderStrategy fieldReaderStrategy = createReaderStrategy(conversion);
                    FieldWriterStrategy fieldWriterStrategy = createWriterStrategy(conversion);
                    List<FieldConversionPair> fieldConversionPairs = new ArrayList<>();
                    conversion
                            .getFields()
                            .forEach(fieldConversionConfig -> {
                                FieldReaderAdapter fieldReader = createFieldReader(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, fieldReaderStrategy);
                                FieldWriterAdapter fieldWriter = createFieldWriter(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, fieldWriterStrategy);
                                fieldConversionPairs.add(new FieldConversionPair(fieldReader, fieldWriter));
                            });
                    converters.put(conversion.getName(), new ModelFieldAdapter(fieldConversionPairs));
                });
        return converters;
    }

    private FieldReaderStrategy createReaderStrategy(ModelConversionConfig conversion) {
        String sourceType = conversion.getSourceReaderType();
        return readerStrategies.get(sourceType);
    }

    private FieldReaderAdapter createFieldReader(
            LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig,
            LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig,
            FieldCoversionConfig fieldConversion,
            FieldReaderStrategy fieldReaderStrategy
    ) {
        String sourceFieldName = fieldConversion.getSource();
        ModelFieldConfig fieldConfig = tryGetField(
                sourceModelFieldConfig, fieldConversion.getSource(),
                targetModelFieldConfig, fieldConversion.getTarget()
        );
        String sourceFieldType = fieldConfig.getType();
        return fieldReaderStrategy.createOne(sourceFieldName, sourceFieldType);
    }

    private FieldWriterStrategy createWriterStrategy(ModelConversionConfig conversion) {
        String targetType = conversion.getTargetReaderType();
        return writerStrategies.get(targetType);
    }

    private FieldWriterAdapter createFieldWriter(LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig, LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig, FieldCoversionConfig fieldConversion, FieldWriterStrategy fieldWriterStrategy) {
        String targetFieldName = fieldConversion.getTarget();
        ModelFieldConfig fieldConfig = tryGetField(
                targetModelFieldConfig, fieldConversion.getTarget(),
                sourceModelFieldConfig, fieldConversion.getSource()
        );
        String targetFieldType = fieldConfig.getType();

        return fieldWriterStrategy.createOne(targetFieldName, targetFieldType);
    }

    private ModelFieldConfig tryGetField(
            LinkedHashMap<String, ModelFieldConfig> modelFieldConfig1, String fieldName1,
            LinkedHashMap<String, ModelFieldConfig> modelFieldConfig2, String fieldName2
    ) {
        LinkedHashMap<String, ModelFieldConfig> currentModelFieldConfig = modelFieldConfig1 != null
                ? modelFieldConfig1
                : modelFieldConfig2;
        return currentModelFieldConfig.containsKey(fieldName1)
                ? currentModelFieldConfig.get(fieldName1)
                : currentModelFieldConfig.get(fieldName2);
    }
}
