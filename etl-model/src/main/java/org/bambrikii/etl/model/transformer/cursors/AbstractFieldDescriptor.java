package org.bambrikii.etl.model.transformer.cursors;

public abstract class AbstractFieldDescriptor<K, D extends AbstractFieldDescriptor> {
    public static final String ARRAY_SUFFIX = "[]";

    protected final K distinctName;
    private final boolean array;
    private final D parent;

    public AbstractFieldDescriptor(K distinctName, boolean array, D parent) {
        this.distinctName = distinctName;
        this.array = array;
        this.parent = parent;
    }

    public K getDistinctName() {
        return distinctName;
    }

    public boolean isArray() {
        return array;
    }

    public D getParent() {
        return parent;
    }
}
