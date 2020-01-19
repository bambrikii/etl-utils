package org.bambrikii.etl.model.transformer.adapters.java;

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
}
