package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

public class FieldDescriptor {
    private final String simpleName;
    private final String distinctName;
    private final boolean array;
    private final int namePos;
    private final boolean leaf;
    private final FieldDescriptor parent;

    public FieldDescriptor(
            String simpleName, String distinctName,
            int namePos, boolean array,
            boolean leaf,
            FieldDescriptor parent
    ) {
        this.simpleName = simpleName;
        this.distinctName = distinctName;
        this.namePos = namePos;
        this.array = array;
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
}
