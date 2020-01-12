package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;
import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;

import java.lang.reflect.InvocationTargetException;

public class JavaReflectionFieldReaderStrategy<T> extends FieldReaderStrategy<T> {
    private final Class<T> cls;

    public JavaReflectionFieldReaderStrategy(Class<T> cls) {
        this.cls = cls;
    }

    private <O> FieldReaderAdapter<T, O> getField(String name) {
        String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        return (obj) -> {
            try {
                return (O) cls.getMethod(methodName).invoke(obj);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldReaderAdapter<T, String> getStringReader(String name) {
        return getField(name);
    }

    @Override
    protected FieldReaderAdapter<T, Integer> getIntReader(String name) {
        return getField(name);
    }

    @Override
    protected FieldReaderAdapter<T, Double> getDoubleReader(String name) {
        return getField(name);
    }
}
