package org.bambikii.etl.model.transformer.adapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelAdapter<S, T> {
    private final List<FieldTransformerPair> fieldAdapters;

    public ModelAdapter(ArrayList<FieldTransformerPair> transformers) {
        fieldAdapters = Collections.unmodifiableList(transformers);
    }

    public void adapt(S source, T target) {
        for (FieldTransformerPair pair : fieldAdapters) {
            FieldWriterAdapter writer = pair.getFieldWriter();
            FieldReaderAdapter reader = pair.getFieldReader();
            Object value = reader.readField(source);
            writer.writeField(target, value);
        }
    }
}
