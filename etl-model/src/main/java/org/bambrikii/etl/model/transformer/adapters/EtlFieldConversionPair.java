package org.bambrikii.etl.model.transformer.adapters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EtlFieldConversionPair<T, O> {
    private EtlFieldExtractable<T, O> fieldReader;
    private EtlFieldLoadable<T, O> fieldWriter;
}
