package org.bambikii.etl.model.transformer.adapters;

public interface EtlFieldLoadable<W, I> {
    void writeField(W obj, I value);
}
