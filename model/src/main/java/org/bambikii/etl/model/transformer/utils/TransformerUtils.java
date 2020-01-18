package org.bambikii.etl.model.transformer.utils;

public class TransformerUtils {
    private TransformerUtils() {
    }

    public static <T> void tryClose(T resultSet) {
        if (resultSet != null && resultSet instanceof AutoCloseable) {
            try {
                ((AutoCloseable) resultSet).close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
