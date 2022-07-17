package org.bambrikii.etl.model.transformer.utils;

public class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T> void tryClose(T resultSet) {
        if (resultSet == null) {
            return;
        }
        if (!(resultSet instanceof AutoCloseable)) {
            return;
        }
        try {
            ((AutoCloseable) resultSet).close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new RuntimeException("Failed to instantiate class " + cls);
        }
    }
}
