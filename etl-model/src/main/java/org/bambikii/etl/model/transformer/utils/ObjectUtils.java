package org.bambikii.etl.model.transformer.utils;

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
}
