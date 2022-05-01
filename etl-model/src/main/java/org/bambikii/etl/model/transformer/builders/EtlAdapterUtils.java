package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.mapping.model.MappingField;
import org.bambikii.etl.model.transformer.schema.model.SchemaField;

import java.util.LinkedHashMap;

public class EtlAdapterUtils {
    private EtlAdapterUtils() {
    }

    static SchemaField tryGetField(
            LinkedHashMap<String, SchemaField> modelFieldConfig1, String fieldName1,
            LinkedHashMap<String, SchemaField> modelFieldConfig2, String fieldName2
    ) {
        LinkedHashMap<String, SchemaField> currentModelFieldConfig = modelFieldConfig1 != null
                ? modelFieldConfig1
                : modelFieldConfig2;
        return currentModelFieldConfig.containsKey(fieldName1)
                ? currentModelFieldConfig.get(fieldName1)
                : currentModelFieldConfig.get(fieldName2);
    }


    static EtlFieldExtractable createFieldReader(
            LinkedHashMap<String, SchemaField> sourceModelFieldConfig,
            LinkedHashMap<String, SchemaField> targetModelFieldConfig,
            MappingField mappingField,
            EtlFieldReader fieldReader
    ) {
        String sourceFieldName = mappingField.getSource();
        SchemaField fieldConfig = tryGetField(
                sourceModelFieldConfig, mappingField.getSource(),
                targetModelFieldConfig, mappingField.getTarget()
        );
        String sourceFieldType = fieldConfig.getType();
        return fieldReader.createOne(sourceFieldName, sourceFieldType);
    }

    static EtlFieldLoadable createFieldWriter(LinkedHashMap<String, SchemaField> sourceModelFieldConfig, LinkedHashMap<String, SchemaField> targetModelFieldConfig, MappingField fieldConversion, EtlFieldWriter fieldWriter) {
        String targetFieldName = fieldConversion.getTarget();
        SchemaField fieldConfig = tryGetField(
                targetModelFieldConfig, fieldConversion.getTarget(),
                sourceModelFieldConfig, fieldConversion.getSource()
        );
        String targetFieldType = fieldConfig.getType();

        return fieldWriter.createOne(targetFieldName, targetFieldType);
    }
}
