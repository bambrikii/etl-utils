package org.bambikii.etl.model.transformer.adapters;

public interface ModelInputFactory<T> {
    T create();

    boolean next(T resultSet);
}
