package org.bambikii.etl.model.transformer.adapters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.bambikii.etl.model.transformer.utils.ObjectUtils.tryClose;

public class EtlModelAdapter<S, T> {
    private final List<EtlFieldConversionPair> fieldConverters;

    public EtlModelAdapter(EtlFieldConversionPair... fieldConverters) {
        this(Arrays.asList(fieldConverters));
    }

    public EtlModelAdapter(List<EtlFieldConversionPair> transformers) {
        fieldConverters = Collections.unmodifiableList(transformers);
    }

    public void adapt(S source, T target) {
        for (EtlFieldConversionPair pair : fieldConverters) {
            EtlFieldLoadable writer = pair.getFieldWriter();
            EtlFieldExtractable reader = pair.getFieldReader();
            Object value = reader.readField(source);
            writer.writeField(target, value);
        }
    }

    public void adapt(
            EtlModelInputFactory<S> inputFactory,
            EtlModelOutputFactory<T> outputFactory
    ) {
        S resultSet = null;
        try {
            resultSet = inputFactory.create();
            while (inputFactory.next(resultSet)) {
                T target = null;
                try {
                    target = outputFactory.create();
                    adapt(resultSet, target);
                    outputFactory.complete(target);
                } finally {
                    tryClose(target);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to move next.", ex);
        } finally {
            tryClose(resultSet);
        }
    }
}
