package org.bambikii.etl.model.transformer.adapters;

public interface ModelOutputFactory<R> {
    R create();

    boolean complete(R statement);
}
