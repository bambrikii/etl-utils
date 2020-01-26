package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import java.util.HashMap;
import java.util.Map;

class Cursor {
    private final FieldDescriptor fieldDescriptor;
    private final int size;
    private final Cursor parentCursor;
    private Map<String, Cursor> children = new HashMap<>();

    private int currentPosition = -1;

    public Cursor(FieldDescriptor fieldDescriptor, int size, Cursor parentCursor) {
        this.fieldDescriptor = fieldDescriptor;
        this.size = size;
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

    public int getSize() {
        return size;
    }

    public Cursor getParentCursor() {
        return parentCursor;
    }

    public boolean canRead() {
        if (size <= 0) {
            return false;
        }
        if (currentPosition < 0) {
            return false;
        }
        if (currentPosition >= size) {
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
        return currentPosition + 1 < size;
    }

    public void addChild(Cursor childCursor) {
        children.put(childCursor.getFieldDescriptor().getDistinctName(), childCursor);
    }
}
