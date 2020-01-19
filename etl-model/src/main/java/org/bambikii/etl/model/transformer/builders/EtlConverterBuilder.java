package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldAdapter;
import org.bambikii.etl.model.transformer.config.EtlConfigMarshaller;
import org.bambikii.etl.model.transformer.config.model.*;

import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EtlConverterBuilder {
    private final Map<String, EtlFieldReaderStrategy> readerStrategies;
    private final Map<String, EtlFieldWriterStrategy> writerStrategies;
    private ConversionRootConfig conversionRoot;
    private Map<String, LinkedHashMap<String, ModelFieldConfig>> modelFieldConfigs;

    public EtlConverterBuilder() {
        this.readerStrategies = new HashMap<>();
        this.writerStrategies = new HashMap<>();
    }

    public EtlConverterBuilder readerStrategy(String name, EtlFieldReaderStrategy strategy) {
        readerStrategies.put(name, strategy);
        return this;
    }

    public EtlConverterBuilder writerStrategy(String name, EtlFieldWriterStrategy strategy) {
        writerStrategies.put(name, strategy);
        return this;
    }

    public EtlConverterBuilder modelConfig(InputStream modelInputStream) throws JAXBException {
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

    public EtlConverterBuilder converterConfig(InputStream conversionInputStream) throws JAXBException {
        conversionRoot = EtlConfigMarshaller.unmarshalConversionConfig(conversionInputStream);
        return this;
    }

    public Map<String, EtlFieldAdapter> build() {
        Map<String, EtlFieldAdapter> converters = new HashMap<>();
        conversionRoot
                .getModelConversionConfigs()
                .forEach(conversion -> {
                    String sourceModel = conversion.getSourceModel();
                    String targetModel = conversion.getTargetModel();
                    LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig = modelFieldConfigs.get(sourceModel);
                    LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig = modelFieldConfigs.get(targetModel);
                    EtlFieldReaderStrategy etlFieldReaderStrategy = createReaderStrategy(conversion);
                    EtlFieldWriterStrategy etlFieldWriterStrategy = createWriterStrategy(conversion);
                    List<EtlFieldConversionPair> etlFieldConversionPairs = new ArrayList<>();
                    conversion
                            .getFields()
                            .forEach(fieldConversionConfig -> {
                                EtlFieldExtractable fieldReader = createFieldReader(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, etlFieldReaderStrategy);
                                EtlFieldLoadable fieldWriter = createFieldWriter(sourceModelFieldConfig, targetModelFieldConfig, fieldConversionConfig, etlFieldWriterStrategy);
                                etlFieldConversionPairs.add(new EtlFieldConversionPair(fieldReader, fieldWriter));
                            });
                    converters.put(conversion.getName(), new EtlFieldAdapter(etlFieldConversionPairs));
                });
        return converters;
    }

    private EtlFieldReaderStrategy createReaderStrategy(ModelConversionConfig conversion) {
        String sourceType = conversion.getSourceReaderType();
        return readerStrategies.get(sourceType);
    }

    private EtlFieldExtractable createFieldReader(
            LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig,
            LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig,
            FieldCoversionConfig fieldConversion,
            EtlFieldReaderStrategy etlFieldReaderStrategy
    ) {
        String sourceFieldName = fieldConversion.getSource();
        ModelFieldConfig fieldConfig = tryGetField(
                sourceModelFieldConfig, fieldConversion.getSource(),
                targetModelFieldConfig, fieldConversion.getTarget()
        );
        String sourceFieldType = fieldConfig.getType();
        return etlFieldReaderStrategy.createOne(sourceFieldName, sourceFieldType);
    }

    private EtlFieldWriterStrategy createWriterStrategy(ModelConversionConfig conversion) {
        String targetType = conversion.getTargetReaderType();
        return writerStrategies.get(targetType);
    }

    private EtlFieldLoadable createFieldWriter(LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig, LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig, FieldCoversionConfig fieldConversion, EtlFieldWriterStrategy etlFieldWriterStrategy) {
        String targetFieldName = fieldConversion.getTarget();
        ModelFieldConfig fieldConfig = tryGetField(
                targetModelFieldConfig, fieldConversion.getTarget(),
                sourceModelFieldConfig, fieldConversion.getSource()
        );
        String targetFieldType = fieldConfig.getType();

        return etlFieldWriterStrategy.createOne(targetFieldName, targetFieldType);
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
