package org.bambrikii.etl.model.transformer.adapters.java.reflection;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;

import java.lang.reflect.InvocationTargetException;

class EtlJavaReflectionFieldReaderStrategy<T> extends EtlFieldReaderStrategy<T> {
    private final Class<T> cls;

    public EtlJavaReflectionFieldReaderStrategy(Class<T> cls) {
        this.cls = cls;
    }

    private <O> EtlFieldExtractable<T, O> getField(String name) {
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
    protected EtlFieldExtractable<T, String> getStringReader(String name) {
        return getField(name);
    }

    @Override
    protected EtlFieldExtractable<T, Integer> getIntReader(String name) {
        return getField(name);
    }

    @Override
    protected EtlFieldExtractable<T, Double> getDoubleReader(String name) {
        return getField(name);
    }
}
