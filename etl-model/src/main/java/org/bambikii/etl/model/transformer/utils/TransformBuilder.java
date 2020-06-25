package org.bambikii.etl.model.transformer.utils;

import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.DOUBLE;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.INT;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.STRING;

public class TransformBuilder {
    private EtlFieldReaderStrategy fieldReader;
    private EtlFieldWriterStrategy fieldWriter;
    private List<EtlFieldConversionPair> fieldMaps = new ArrayList<>();
    private EtlModelInputFactory modelReader;
    private EtlModelOutputFactory modelWriter;

    public TransformBuilder fieldReader(EtlFieldReaderStrategy fieldReader) {
        this.fieldReader = fieldReader;
        return this;
    }

    public TransformBuilder fieldWriter(EtlFieldWriterStrategy fieldWriter) {
        this.fieldWriter = fieldWriter;
        return this;
    }

    public TransformBuilder modelReader(EtlModelInputFactory modelReader) {
        this.modelReader = modelReader;
        return this;
    }

    public TransformBuilder modelWriter(EtlModelOutputFactory modelWriter) {
        this.modelWriter = modelWriter;
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
}
