package org.bambrikii.etl.model.transformer.adapters.java.map;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;

import java.util.Map;

import static org.bambrikii.etl.model.transformer.adapters.java.map.EtlJavaMapFieldReaderStrategy.ETL_JAVA_MAP;

class EtlJavaMapFieldWriterStrategy extends EtlFieldWriterStrategy<Map<String, Object>> implements EtlNamable {
    @Override
    public String getName() {
        return ETL_JAVA_MAP;
    }

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
