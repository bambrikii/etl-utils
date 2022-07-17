package org.bambrikii.etl.model.transformer.builders;

import org.bambrikii.etl.model.transformer.adapters.EtlFieldLoadable;

import java.util.Map;

import static org.bambrikii.etl.model.transformer.builders.EtlFieldReader.*;

public abstract class EtlFieldWriter<T> {
    public EtlFieldLoadable<T, ?> createOne(String name, String type) {
        switch (type) {
            case STRING:
                return getStringWriter(name);
            case INT:
                return getIntWriter(name);
            case DOUBLE:
                return getDoubleWriter(name);
            case MAP:
                return getMapWriter(name);
            default:
                return getMoreWriter(name, type);
        }
    }

    protected abstract EtlFieldLoadable<T, String> getStringWriter(String name);

    protected abstract EtlFieldLoadable<T, Integer> getIntWriter(String name);

    protected abstract EtlFieldLoadable<T, Double> getDoubleWriter(String name);

    protected abstract EtlFieldLoadable<T, Map<String, Object>> getMapWriter(String name);

    protected <O> EtlFieldLoadable<T, O> getMoreWriter(String name, String type) {
        throw new IllegalStateException("Unexpected value: " + name + ", " + type);
    }
}
