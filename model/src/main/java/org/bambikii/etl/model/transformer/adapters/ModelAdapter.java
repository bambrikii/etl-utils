package org.bambikii.etl.model.transformer.adapters;

import java.util.Collections;
import java.util.List;

public class ModelAdapter<S, T> {
    private final List<FieldConversionPair> fieldAdapters;

    public ModelAdapter(List<FieldConversionPair> transformers) {
        fieldAdapters = Collections.unmodifiableList(transformers);
    }

    public void adapt(S source, T target) {
        for (FieldConversionPair pair : fieldAdapters) {
            FieldWriterAdapter writer = pair.getFieldWriter();
            FieldReaderAdapter reader = pair.getFieldReader();
            Object value = reader.readField(source);
            writer.writeField(target, value);
        }
    }
}
