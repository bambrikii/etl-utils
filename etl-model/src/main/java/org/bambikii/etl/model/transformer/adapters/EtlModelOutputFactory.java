package org.bambikii.etl.model.transformer.adapters;

public interface EtlModelOutputFactory<R> {
    R create();

    boolean complete(R statement);
}
