package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.bambikii.etl.model.transformer.cursors.AbstractCursor;

public class PojoWriteCursor extends AbstractCursor<String, PojoWriteCursor, PojoFieldDescriptor> {
    private int size;

    public PojoWriteCursor(PojoFieldDescriptor fieldDescriptor, PojoWriteCursor parentCursor) {
        super(fieldDescriptor, parentCursor);
    }

    public int advanceSize() {
        return ++size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
