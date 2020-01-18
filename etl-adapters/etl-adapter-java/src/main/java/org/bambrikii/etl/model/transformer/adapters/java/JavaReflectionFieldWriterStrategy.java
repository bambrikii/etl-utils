package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;

import java.lang.reflect.InvocationTargetException;

public class JavaReflectionFieldWriterStrategy<T> extends FieldWriterStrategy<T> {
    private final Class<T> cls;

    public JavaReflectionFieldWriterStrategy(Class<T> cls) {
        this.cls = cls;
    }

    private FieldWriterAdapter setField(String name, Class<?> param) {
        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        return (obj, value) -> {
            try {
                cls.getMethod(methodName, param).invoke(obj, value);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldWriterAdapter<T, String> getStringWriter(String name) {
        return setField(name, String.class);
    }

    @Override
    protected FieldWriterAdapter<T, Integer> getIntWriter(String name) {
        return setField(name, Integer.class);
    }

    @Override
    protected FieldWriterAdapter<T, Double> getDoubleWriter(String name) {
        return setField(name, Double.class);
    }
}
