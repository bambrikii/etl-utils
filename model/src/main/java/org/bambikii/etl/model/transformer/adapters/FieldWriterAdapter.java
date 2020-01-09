package org.bambikii.etl.model.transformer.adapters;

public interface FieldWriterAdapter<T, I> {
    void writeField(T obj, I value);
}
