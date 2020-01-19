package org.bambikii.etl.model.transformer.adapters;

import java.util.Collections;
import java.util.List;

public class EtlFieldAdapter<S, T> {
    private final List<EtlFieldConversionPair> fieldAdapters;

    public EtlFieldAdapter(List<EtlFieldConversionPair> transformers) {
        fieldAdapters = Collections.unmodifiableList(transformers);
    }

    public void adapt(S source, T target) {
        for (EtlFieldConversionPair pair : fieldAdapters) {
            EtlFieldLoadable writer = pair.getFieldWriter();
            EtlFieldExtractable reader = pair.getFieldReader();
            Object value = reader.readField(source);
            writer.writeField(target, value);
        }
    }
}
