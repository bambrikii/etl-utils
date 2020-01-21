package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

class Cursor {
    public static final int UNDEFINED_POSITION = -1;

    private final FieldDescriptor fieldDescriptor;
    private final int size;
    private final Cursor parentCursor;

    private int currentPosition = UNDEFINED_POSITION;

    public Cursor(FieldDescriptor fieldDescriptor, int size, Cursor parentCursor) {
        this.fieldDescriptor = fieldDescriptor;
        this.size = size;
        this.parentCursor = parentCursor;
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
        if (currentPosition == UNDEFINED_POSITION) {
            return false;
        }
        if (currentPosition > size) {
            return false;
        }
        return true;
    }

    public boolean next() {
        if (currentPosition == UNDEFINED_POSITION) {
            currentPosition = 0;
        }
        if (currentPosition >= size) {
            return false;
        }
        currentPosition++;
        return true;
    }
}
