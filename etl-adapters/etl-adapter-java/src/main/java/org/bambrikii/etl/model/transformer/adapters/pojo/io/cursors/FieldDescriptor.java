package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

public class FieldDescriptor {
    private final String distinctName;
    private final int namePos;
    private final boolean leaf;
    private final FieldDescriptor parent;
    private final FieldNameElement nameElement;

    public FieldDescriptor(
            String simpleName, String distinctName,
            int namePos,
            boolean leaf, boolean isArray,
            FieldDescriptor parent
    ) {
        this(new FieldNameElement(simpleName, null, isArray), distinctName, namePos, leaf, parent);
    }

    public FieldDescriptor(
            FieldNameElement nameElement, String distinctName,
            int namePos,
            boolean leaf,
            FieldDescriptor parent
    ) {
        this.nameElement = nameElement;
        this.distinctName = distinctName;
        this.namePos = namePos;
        this.leaf = leaf;
        this.parent = parent;
    }

    public String getSimpleName() {
        return nameElement.getSimpleName();
    }

    public String getDistinctName() {
        return distinctName;
    }

    public int getNamePos() {
        return namePos;
    }

    public boolean isArray() {
        return nameElement.isArray();
    }

    public boolean isLeaf() {
        return leaf;
    }

    public FieldDescriptor getParent() {
        return parent;
    }

    public Class<?> getType() {
        return nameElement.getType();
    }
}
