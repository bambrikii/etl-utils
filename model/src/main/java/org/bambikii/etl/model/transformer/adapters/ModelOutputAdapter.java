package org.bambikii.etl.model.transformer.adapters;

public interface ModelOutputAdapter<T> {
    <T> T next();
}
