package org.bambrikii.etl.model.transformer.cursors;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCursor<K, T extends AbstractCursor, D extends AbstractFieldDescriptor<K, D>> {
    private final D fieldDescriptor;
    private final T parentCursor;
    private Map<K, T> children = new HashMap<>();

    private int currentPosition = 0;

    public AbstractCursor(D fieldDescriptor, T parentCursor) {
        this.fieldDescriptor = fieldDescriptor;
        this.parentCursor = parentCursor;
        if (parentCursor != null) {
            parentCursor.addChild(this);
        }
    }

    public D getFieldDescriptor() {
        return fieldDescriptor;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public abstract int getSize();

    public T getParentCursor() {
        return parentCursor;
    }

    public boolean hasCurrent() {
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
        AbstractFieldDescriptor fieldDescriptor = childCursor.getFieldDescriptor();
        K distinctName = (K) fieldDescriptor.getDistinctName();
        children.put(distinctName, childCursor);
    }
}
