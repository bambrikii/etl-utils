package org.bambrikii.etl.model.transformer.adapters.java.reflection;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;

import java.lang.reflect.InvocationTargetException;

import static org.bambrikii.etl.model.transformer.adapters.java.reflection.EtlJavaReflectionFieldReaderStrategy.ETL_JAVA_REFLECTION_NAME;

class EtlJavaReflectionFieldWriterStrategy<T> extends EtlFieldWriterStrategy<T> implements EtlNamable {
    private final Class<T> cls;

    public EtlJavaReflectionFieldWriterStrategy(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public String getName() {
        return ETL_JAVA_REFLECTION_NAME;
    }

    private EtlFieldLoadable setField(String name, Class<?> param) {
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
    protected EtlFieldLoadable<T, String> getStringWriter(String name) {
        return setField(name, String.class);
    }

    @Override
    protected EtlFieldLoadable<T, Integer> getIntWriter(String name) {
        return setField(name, Integer.class);
    }

    @Override
    protected EtlFieldLoadable<T, Double> getDoubleWriter(String name) {
        return setField(name, Double.class);
    }
}
