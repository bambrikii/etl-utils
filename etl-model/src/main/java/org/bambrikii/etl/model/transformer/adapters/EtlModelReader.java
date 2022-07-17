package org.bambrikii.etl.model.transformer.adapters;

import org.bambrikii.etl.model.transformer.builders.EtlFieldReader;

public interface EtlModelReader<T> {
    T create();

    boolean next(T resultSet);

    EtlFieldReader<T> createFieldReader();
}
