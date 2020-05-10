package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCursor<T extends AbstractCursor> {
    private final FieldDescriptor fieldDescriptor;
    private final T parentCursor;
    private Map<String, T> children = new HashMap<>();

    private int currentPosition = 0;

    public AbstractCursor(FieldDescriptor fieldDescriptor, T parentCursor) {
        this.fieldDescriptor = fieldDescriptor;
        this.parentCursor = parentCursor;
        if (parentCursor != null) {
            parentCursor.addChild(this);
        }
    }

    public FieldDescriptor getFieldDescriptor() {
        return fieldDescriptor;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public abstract int getSize();

    public T getParentCursor() {
        return parentCursor;
    }

    public boolean canRead() {
        if (getSize() <= 0) {
            return false;
        }
        if (currentPosition < 0) {
            return false;
        }
        if (currentPosition >= getSize()) {
            return false;
        }
        return true;
    }

    public boolean next() {
        if (!hasNext()) {
            return false;
        }
        currentPosition++;
        return true;
    }

    public boolean hasNext() {
        return currentPosition + 1 < getSize();
    }

    public void addChild(T childCursor) {
        children.put(childCursor.getFieldDescriptor().getDistinctName(), childCursor);
    }
}
