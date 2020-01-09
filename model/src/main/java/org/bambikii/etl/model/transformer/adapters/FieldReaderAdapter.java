package org.bambikii.etl.model.transformer.adapters;

public interface FieldReaderAdapter<T, O> {
    O readField(T obj);
}
