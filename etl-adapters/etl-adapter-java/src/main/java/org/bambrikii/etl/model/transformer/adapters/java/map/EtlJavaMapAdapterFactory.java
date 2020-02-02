package org.bambrikii.etl.model.transformer.adapters.java.map;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

import java.util.Map;

public class EtlJavaMapAdapterFactory {
    private EtlJavaMapAdapterFactory() {
    }

    public static EtlFieldReaderStrategy<Map<String, Object>> createFieldReader() {
        return new EtlJavaMapFieldReaderStrategy();
    }

    public static EtlFieldWriterStrategy<Map<String, Object>> createFieldWriter() {
        return new EtlJavaMapFieldWriterStrategy();
    }

    public static EtlModelInputFactory<Map<String, Object>> createJavaInputAdapter(Map<String, Object> source) {
        return new EtlModelInputFactory<Map<String, Object>>() {
            boolean shouldProceed = true;

            @Override
            public Map<String, Object> create() {
                return source;
            }

            @Override
            public boolean next(Map<String, Object> resultSet) {
                if (!shouldProceed) {
                    return false;
                }
                shouldProceed = false;
                return true;
            }
        };
    }

    public static EtlModelOutputFactory<Map<String, Object>> createJavaOutputAdapter(Map<String, Object> target) {
        return new EtlModelOutputFactory<Map<String, Object>>() {
            @Override
            public Map<String, Object> create() {
                return target;
            }

            @Override
            public boolean complete(Map<String, Object> statement) {
                return true;
            }
        };
    }
}
