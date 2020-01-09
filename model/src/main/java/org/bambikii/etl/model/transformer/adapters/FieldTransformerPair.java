package org.bambikii.etl.model.transformer.adapters;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldTransformerPair<T, O> {
    private FieldReaderAdapter<T, O> fieldReader;
    private FieldWriterAdapter<T, O> fieldWriter;
}
