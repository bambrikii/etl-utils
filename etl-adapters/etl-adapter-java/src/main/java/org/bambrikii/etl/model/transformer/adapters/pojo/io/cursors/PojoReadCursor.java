package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.bambrikii.etl.model.transformer.cursors.AbstractCursor;

public class PojoReadCursor extends AbstractCursor<String, PojoReadCursor, PojoFieldDescriptor> {
    private final int size;

    public PojoReadCursor(PojoFieldDescriptor fieldDescriptor, int size, PojoReadCursor parentCursor) {
        super(fieldDescriptor, parentCursor);
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
