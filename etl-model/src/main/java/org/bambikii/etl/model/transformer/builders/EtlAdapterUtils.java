package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.config.model.FieldConversionConfig;
import org.bambikii.etl.model.transformer.config.model.ModelFieldConfig;

import java.util.LinkedHashMap;

public class EtlAdapterUtils {
    private EtlAdapterUtils() {
    }

    static ModelFieldConfig tryGetField(
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


    static EtlFieldExtractable createFieldReader(
            LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig,
            LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig,
            FieldConversionConfig fieldConversion,
            EtlFieldReader fieldReader
    ) {
        String sourceFieldName = fieldConversion.getSource();
        ModelFieldConfig fieldConfig = tryGetField(
                sourceModelFieldConfig, fieldConversion.getSource(),
                targetModelFieldConfig, fieldConversion.getTarget()
        );
        String sourceFieldType = fieldConfig.getType();
        return fieldReader.createOne(sourceFieldName, sourceFieldType);
    }

    static EtlFieldLoadable createFieldWriter(LinkedHashMap<String, ModelFieldConfig> sourceModelFieldConfig, LinkedHashMap<String, ModelFieldConfig> targetModelFieldConfig, FieldConversionConfig fieldConversion, EtlFieldWriter fieldWriter) {
        String targetFieldName = fieldConversion.getTarget();
        ModelFieldConfig fieldConfig = tryGetField(
                targetModelFieldConfig, fieldConversion.getTarget(),
                sourceModelFieldConfig, fieldConversion.getSource()
        );
        String targetFieldType = fieldConfig.getType();

        return fieldWriter.createOne(targetFieldName, targetFieldType);
    }
}
