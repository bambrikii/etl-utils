package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;

import static org.bambikii.etl.model.transformer.builders.FieldReaderStrategy.*;

public abstract class FieldWriterStrategy<T> {
    public FieldWriterAdapter<T, ?> createOne(String name, String type) {
        switch (type) {
            case STRING:
                return getStringWriter(name);
            case INT:
                return getIntWriter(name);
            case DOUBLE:
                return getDoubleWriter(name);
            default:
                return getMoreWriter(name, type);
        }
    }

    protected abstract FieldWriterAdapter<T, String> getStringWriter(String name);

    protected abstract FieldWriterAdapter<T, Integer> getIntWriter(String name);

    protected abstract FieldWriterAdapter<T, Double> getDoubleWriter(String name);

    protected <O> FieldWriterAdapter<T, O> getMoreWriter(String name, String type) {
        throw new IllegalStateException("Unexpected value: " + name + ", " + type);
    }
}