package org.bambrikii.etl.model.transformer.adapters;

import org.bambrikii.etl.model.transformer.utils.ObjectUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            EtlModelReader<S> inputFactory,
            EtlModelWriter<T> outputFactory
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
                    ObjectUtils.tryClose(target);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to move next.", ex);
        } finally {
            ObjectUtils.tryClose(resultSet);
        }
    }
}
