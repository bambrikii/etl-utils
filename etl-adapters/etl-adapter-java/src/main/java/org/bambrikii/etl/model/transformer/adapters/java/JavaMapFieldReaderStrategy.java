package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;
import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;

import java.util.Map;

public class JavaMapFieldReaderStrategy<T> extends FieldReaderStrategy<Map<String, Object>> {
    @Override
    protected FieldReaderAdapter<Map<String, Object>, String> getStringReader(String name) {
        return (obj) -> (String) obj.get(name);
    }

    @Override
    protected FieldReaderAdapter<Map<String, Object>, Integer> getIntReader(String name) {
        return (obj) -> (Integer) obj.get(name);
    }

    @Override
    protected FieldReaderAdapter<Map<String, Object>, Double> getDoubleReader(String name) {
        return (obj) -> (Double) obj.get(name);
    }
}
