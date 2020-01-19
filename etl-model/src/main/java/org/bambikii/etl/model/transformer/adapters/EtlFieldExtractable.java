package org.bambikii.etl.model.transformer.adapters;

public interface EtlFieldExtractable<R, O> {
    O readField(R obj);
}
