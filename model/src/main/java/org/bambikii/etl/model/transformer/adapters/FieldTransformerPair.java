package org.bambikii.etl.model.transformer.adapters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldTransformerPair<T, O> {
    private FieldReaderAdapter<T, O> fieldReader;
    private FieldWriterAdapter<T, O> fieldWriter;
}
