package org.bambikii.etl.model.transformer.adapters;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModelInputAdapter<T extends AutoCloseable, R extends AutoCloseable> {
    private final Supplier<R> outputSupplier;
    private final Predicate<T> inputHasNext;
    private final Supplier<T> inputSupplier;
    private final ModelFieldAdapter modelFieldAdapter;

    public ModelInputAdapter(
            ModelFieldAdapter modelFieldAdapter, Supplier<T> inputSupplier,
            Predicate<T> inputHasNext,
            Supplier<R> outputSupplier
    ) {
        this.inputSupplier = inputSupplier;
        this.outputSupplier = outputSupplier;
        this.inputHasNext = inputHasNext;
        this.modelFieldAdapter = modelFieldAdapter;
    }

    public void adapt() {
        try (T rs = inputSupplier.get()) {
            while (inputHasNext.test(rs)) {
                modelFieldAdapter.adapt(rs, outputSupplier.get());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to move next.", ex);
        }
    }

    public static <T extends AutoCloseable, R extends AutoCloseable> void adapt(
            ModelFieldAdapter modelFieldAdapter,
            Supplier<T> inputSupplier, Predicate<T> inputHasNext,
            Supplier<R> outputSupplier
    ) {
        try (T rs = inputSupplier.get()) {
            while (inputHasNext.test(rs)) {
                modelFieldAdapter.adapt(rs, outputSupplier.get());
                outputSupplier.complete();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to move next.", ex);
        }
    }
}
