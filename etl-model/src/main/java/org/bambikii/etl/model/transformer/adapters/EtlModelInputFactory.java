package org.bambikii.etl.model.transformer.adapters;

public interface EtlModelInputFactory<T> {
    T create();

    boolean next(T resultSet);
}
