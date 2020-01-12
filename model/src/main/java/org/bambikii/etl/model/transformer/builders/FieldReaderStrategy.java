package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;

public abstract class FieldReaderStrategy<T> {
    public static final String STRING = "STRING";
    public static final String INT = "INT";
    public static final String DOUBLE = "DOUBLE";

    public FieldReaderAdapter<T, ?> createOne(String name, String type) {
        switch (type) {
            case STRING:
                return getStringReader(name);
            case INT:
                return getIntReader(name);
            case DOUBLE:
                return getDoubleReader(name);
            default:
                return getMoreReader(name, type);
        }
    }

    protected abstract FieldReaderAdapter<T, String> getStringReader(String name);

    protected abstract FieldReaderAdapter<T, Integer> getIntReader(String name);

    protected abstract FieldReaderAdapter<T, Double> getDoubleReader(String name);

    protected <O> FieldReaderAdapter<T, O> getMoreReader(String name, String type) {
        throw new IllegalStateException("Unexpected value: " + name + ", " + type);
    }
}