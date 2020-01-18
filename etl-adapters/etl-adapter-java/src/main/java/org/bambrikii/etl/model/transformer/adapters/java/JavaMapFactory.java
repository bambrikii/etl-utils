package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;

import java.util.Map;

public class JavaMapFactory {
    private JavaMapFactory() {
    }

    public static FieldReaderStrategy<Map<String, Object>> fieldReader() {
        return new JavaMapFieldReaderStrategy();
    }

    public static FieldWriterStrategy<Map<String, Object>> fieldWriter() {
        return new JavaMapFieldWriterStrategy();
    }
}
