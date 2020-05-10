package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import java.util.List;

public class FieldNameElement {
    private final String simpleName;
    private final Class<?> type;
    private final boolean array;

    private static boolean evaluateArray(Class<?> type) {
        return type != null && List.class.isAssignableFrom(type);
    }

    public FieldNameElement(String simpleName, Class<?> type) {
        this(simpleName, type, evaluateArray(type));
    }

    public FieldNameElement(String simpleName, Class<?> type, boolean isArray) {
        this.simpleName = simpleName;
        this.type = type;
        this.array = isArray || evaluateArray(type);
    }

    public String getSimpleName() {
        return simpleName;
    }

    public boolean isArray() {
        return array;
    }

    public Class<?> getType() {
        return type;
    }
}
