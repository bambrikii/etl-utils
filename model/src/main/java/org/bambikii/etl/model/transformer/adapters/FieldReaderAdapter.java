package org.bambikii.etl.model.transformer.adapters;

public interface FieldReaderAdapter<R, O> {
    O readField(R obj);
}
