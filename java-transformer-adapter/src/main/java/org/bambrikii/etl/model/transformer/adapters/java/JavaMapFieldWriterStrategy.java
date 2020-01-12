package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;

import java.util.Map;

public class JavaMapFieldWriterStrategy extends FieldWriterStrategy<Map<String, Object>> {
    @Override
    protected FieldWriterAdapter<Map<String, Object>, String> getStringWriter(String name) {
        return (obj, value) -> obj.put(name, value);
    }

    @Override
    protected FieldWriterAdapter<Map<String, Object>, Integer> getIntWriter(String name) {
        return (obj, value) -> obj.put(name, value);
    }

    @Override
    protected FieldWriterAdapter<Map<String, Object>, Double> getDoubleWriter(String name) {
        return (obj, value) -> obj.put(name, value);
    }
}
