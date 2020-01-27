package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import java.util.List;

public class FieldDescriptor {
    private final String simpleName;
    private final String distinctName;
    private final boolean array;
    private final int namePos;
    private final boolean leaf;
    private final FieldDescriptor parent;
    private Class<?> type;

    public FieldDescriptor(
            String simpleName, String distinctName,
            int namePos, boolean array,
            boolean leaf,
            FieldDescriptor parent
    ) {
        this(
                simpleName, distinctName,
                namePos, array, null,
                leaf,
                parent
        );
    }

    public FieldDescriptor(
            String simpleName, String distinctName,
            int namePos, boolean array, Class<?> type,
            boolean leaf,
            FieldDescriptor parent
    ) {
        this.simpleName = simpleName;
        this.distinctName = distinctName;
        this.namePos = namePos;
        this.type = type;
        this.array = array || (type != null && type.isAssignableFrom(List.class));
        this.leaf = leaf;
        this.parent = parent;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getDistinctName() {
        return distinctName;
    }

    public int getNamePos() {
        return namePos;
    }

    public boolean isArray() {
        return array;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public FieldDescriptor getParent() {
        return parent;
    }

    public Class<?> getType() {
        return type;
    }
}
