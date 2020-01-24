package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

public class FieldDescriptor {
    public static final FieldDescriptor NOT_AVAILABLE_FIELD_DESCRIPTOR = new FieldDescriptor(null, null, -1, false, false) {
    };
    private final String simpleName;
    private final String distinctName;
    private final boolean array;
    private final int simpleNamePosition;
    private final boolean leaf;

    public FieldDescriptor(
            String simpleName, String distinctName,
            int simpleNamePosition, boolean array,
            boolean leaf
    ) {
        this.simpleName = simpleName;
        this.distinctName = distinctName;
        this.simpleNamePosition = simpleNamePosition;
        this.array = array;
        this.leaf = leaf;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getDistinctName() {
        return distinctName;
    }

    public int getSimpleNamePosition() {
        return simpleNamePosition;
    }

    public boolean isArray() {
        return array;
    }

    public boolean isLeaf() {
        return leaf;
    }
}
