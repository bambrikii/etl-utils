package org.bambikii.etl.model.transformer.adapters;

public interface FieldWriterAdapter<W, I> {
    void writeField(W obj, I value);
}
