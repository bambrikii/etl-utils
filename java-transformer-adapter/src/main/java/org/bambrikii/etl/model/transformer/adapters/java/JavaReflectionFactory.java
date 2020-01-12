package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;

public class JavaReflectionFactory {
    private JavaReflectionFactory() {
    }

    public static <T> FieldReaderStrategy<Class<T>> fieldReader(Class<T> cls) {
        return new JavaReflectionFieldReaderStrategy(cls);
    }

    public static <T> FieldWriterStrategy<Class<T>> fieldWriter(Class<T> cls) {
        return new JavaReflectionFieldWriterStrategy(cls);
    }
}
