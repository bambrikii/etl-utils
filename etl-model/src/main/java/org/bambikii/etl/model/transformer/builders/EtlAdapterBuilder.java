package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;

import java.util.ArrayList;
import java.util.List;

public class EtlAdapterBuilder {
    private final List<EtlFieldConversionPair> fieldConversionPairs;
    private EtlFieldReaderStrategy readerStrategy;
    private EtlFieldWriterStrategy writerStrategy;

    public EtlAdapterBuilder() {
        this.fieldConversionPairs = new ArrayList<>();
    }

    public EtlAdapterBuilder readerStrategy(EtlFieldReaderStrategy strategy) {
        this.readerStrategy = strategy;
        return this;
    }

    public EtlAdapterBuilder writerStrategy(EtlFieldWriterStrategy strategy) {
        this.writerStrategy = strategy;
        return this;
    }

    public EtlAdapterBuilder addFieldConversion(
            String sourceFieldName, String sourceFieldType,
            String targetFieldName, String targetFieldType
    ) {
        EtlFieldExtractable fieldReader = readerStrategy.createOne(sourceFieldName, sourceFieldType);
        EtlFieldLoadable fieldWriter = writerStrategy.createOne(targetFieldName, targetFieldType);
        fieldConversionPairs.add(new EtlFieldConversionPair<>(fieldReader, fieldWriter));
        return this;
    }

    public EtlAdapterBuilder addFieldConversion(
            String sourceFieldName, String sourceFieldType,
            String targetFieldName
    ) {
        fieldConversionPairs.add(new EtlFieldConversionPair(
                readerStrategy.createOne(sourceFieldName, sourceFieldType),
                writerStrategy.createOne(targetFieldName, sourceFieldType)
        ));
        return this;
    }

    public EtlModelAdapter buildAdapter() {
        return new EtlModelAdapter(new ArrayList<>(fieldConversionPairs));
    }
}
