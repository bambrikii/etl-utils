package org.bambrikii.etl.model.transformer.adapters.java.map;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;

import java.util.Map;

class EtlJavaMapFieldReaderStrategy<T> extends EtlFieldReaderStrategy<Map<String, Object>> {
    @Override
    protected EtlFieldExtractable<Map<String, Object>, String> getStringReader(String name) {
        return (obj) -> (String) obj.get(name);
    }

    @Override
    protected EtlFieldExtractable<Map<String, Object>, Integer> getIntReader(String name) {
        return (obj) -> (Integer) obj.get(name);
    }

    @Override
    protected EtlFieldExtractable<Map<String, Object>, Double> getDoubleReader(String name) {
        return (obj) -> (Double) obj.get(name);
    }
}