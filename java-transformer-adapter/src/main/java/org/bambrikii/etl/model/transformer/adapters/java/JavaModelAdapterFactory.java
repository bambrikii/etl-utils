package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.ModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.ModelOutputFactory;

import java.util.Map;

public class JavaModelAdapterFactory {
    private JavaModelAdapterFactory() {
    }

    public static ModelInputFactory<Map<String, Object>> createJavaInputAdapter(Map<String, Object> source) {
        return new ModelInputFactory<Map<String, Object>>() {
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

    public static ModelOutputFactory<Map<String, Object>> createJavaOutputAdapter(Map<String, Object> target) {
        return new ModelOutputFactory<Map<String, Object>>() {
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
