package org.bambrikii.etl.model.transformer.adapters;

import org.bambrikii.etl.model.transformer.builders.EtlFieldWriter;

public interface EtlModelWriter<R> {
    R create();

    boolean complete(R statement);

    EtlFieldWriter<R> createFieldWriter();
}
