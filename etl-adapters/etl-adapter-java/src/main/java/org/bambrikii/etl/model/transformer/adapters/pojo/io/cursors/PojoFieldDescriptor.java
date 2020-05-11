package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.bambikii.etl.model.transformer.cursors.AbstractFieldDescriptor;

public class PojoFieldDescriptor extends AbstractFieldDescriptor<String, PojoFieldDescriptor> {
    private final int namePos;
    private final boolean leaf;
    private final FieldNameElement nameElement;

    public PojoFieldDescriptor(
            String simpleName, String distinctName,
            int namePos,
            boolean leaf, boolean array,
            PojoFieldDescriptor parent
    ) {
        this(new FieldNameElement(simpleName, null, array), distinctName, namePos, leaf, parent);
    }

    public PojoFieldDescriptor(
            FieldNameElement nameElement, String distinctName,
            int namePos,
            boolean leaf,
            PojoFieldDescriptor parent
    ) {
        super(distinctName, nameElement != null ? nameElement.isArray() : false, parent);
        this.nameElement = nameElement;
        this.namePos = namePos;
        this.leaf = leaf;
    }

    public String getSimpleName() {
        return nameElement.getSimpleName();
    }

    public int getNamePos() {
        return namePos;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public Class<?> getType() {
        return nameElement.getType();
    }
}
