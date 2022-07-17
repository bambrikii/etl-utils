package org.bambrikii.etl.model.transformer.builders;

import org.bambrikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambrikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambrikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambrikii.etl.model.transformer.adapters.EtlModelAdapter;

import java.util.ArrayList;
import java.util.List;

public class EtlAdapterBuilder {
    private final List<EtlFieldConversionPair> fieldConversionPairs;
    private EtlFieldReader reader;
    private EtlFieldWriter writer;

    public EtlAdapterBuilder() {
        this.fieldConversionPairs = new ArrayList<>();
    }

    public EtlAdapterBuilder reader(EtlFieldReader reader) {
        this.reader = reader;
        return this;
    }

    public EtlAdapterBuilder writer(EtlFieldWriter writer) {
        this.writer = writer;
        return this;
    }

    public EtlAdapterBuilder mapping(
            String sourceFieldName, String sourceFieldType,
            String targetFieldName, String targetFieldType
    ) {
        EtlFieldExtractable fieldReader = reader.createOne(sourceFieldName, sourceFieldType);
        EtlFieldLoadable fieldWriter = writer.createOne(targetFieldName, targetFieldType);
        fieldConversionPairs.add(new EtlFieldConversionPair<>(fieldReader, fieldWriter));
        return this;
    }

    public EtlAdapterBuilder mapping(
            String sourceFieldName, String sourceFieldType,
            String targetFieldName
    ) {
        fieldConversionPairs.add(new EtlFieldConversionPair(
                reader.createOne(sourceFieldName, sourceFieldType),
                writer.createOne(targetFieldName, sourceFieldType)
        ));
        return this;
    }

    public EtlModelAdapter buildAdapter() {
        return new EtlModelAdapter(new ArrayList<>(fieldConversionPairs));
    }
}
