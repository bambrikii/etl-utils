package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

import java.util.Map;

public class EtlJavaMapFieldWriterStrategy extends EtlFieldWriterStrategy<Map<String, Object>> {
    @Override
    protected EtlFieldLoadable<Map<String, Object>, String> getStringWriter(String name) {
        return (obj, value) -> obj.put(name, value);
    }

    @Override
    protected EtlFieldLoadable<Map<String, Object>, Integer> getIntWriter(String name) {
        return (obj, value) -> obj.put(name, value);
    }

    @Override
    protected EtlFieldLoadable<Map<String, Object>, Double> getDoubleWriter(String name) {
        return (obj, value) -> obj.put(name, value);
    }
}
