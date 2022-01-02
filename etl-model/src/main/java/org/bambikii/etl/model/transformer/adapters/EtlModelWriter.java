package org.bambikii.etl.model.transformer.adapters;

import org.bambikii.etl.model.transformer.builders.EtlFieldWriter;

public interface EtlModelWriter<R> {
    R create();

    boolean complete(R statement);

    EtlFieldWriter<R> createFieldWriter();
}
