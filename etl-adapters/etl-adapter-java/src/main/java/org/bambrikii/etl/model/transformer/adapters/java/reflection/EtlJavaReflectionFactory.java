package org.bambrikii.etl.model.transformer.adapters.java.reflection;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

public class EtlJavaReflectionFactory {
    private EtlJavaReflectionFactory() {
    }

    public static <T> EtlFieldReaderStrategy<Class<T>> fieldReader(Class<T> cls) {
        return new EtlJavaReflectionFieldReaderStrategy(cls);
    }

    public static <T> EtlFieldWriterStrategy<Class<T>> fieldWriter(Class<T> cls) {
        return new EtlJavaReflectionFieldWriterStrategy(cls);
    }

    public static <T> EtlModelInputFactory<T> createJavaInputAdapter(T source) {
        return new EtlModelInputFactory<T>() {
            boolean shouldProceed = true;

            @Override
            public T create() {
                return source;
            }

            @Override
            public boolean next(T resultSet) {
                if (!shouldProceed) {
                    return false;
                }
                shouldProceed = false;
                return true;
            }
        };
    }

    public static <T> EtlModelOutputFactory<T> createJavaOutputAdapter(T target) {
        return new EtlModelOutputFactory<T>() {
            @Override
            public T create() {
                return target;
            }

            @Override
            public boolean complete(T statement) {
                return true;
            }
        };
    }
}
