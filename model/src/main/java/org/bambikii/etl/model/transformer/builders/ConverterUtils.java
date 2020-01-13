package org.bambikii.etl.model.transformer.builders;

public class ConverterUtils {
    private ConverterUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }
}
