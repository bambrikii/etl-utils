package org.bambikii.etl.model.transformer.adapters;

import static org.bambikii.etl.model.transformer.utils.ObjectUtils.tryClose;

public class EtlUtils {
    private EtlUtils() {
    }

    public static <T, R> void transform(
            EtlFieldAdapter etlFieldAdapter,
            EtlModelInputFactory<T> inputSupplier,
            EtlModelOutputFactory<R> outputSupplier
    ) {
        T resultSet = null;
        try {
            resultSet = inputSupplier.create();
            while (inputSupplier.next(resultSet)) {
                R target = null;
                try {
                    target = outputSupplier.create();
                    etlFieldAdapter.adapt(resultSet, target);
                    outputSupplier.complete(target);
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
