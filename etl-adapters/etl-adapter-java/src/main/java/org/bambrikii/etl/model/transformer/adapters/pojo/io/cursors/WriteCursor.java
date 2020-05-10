package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

public class WriteCursor extends AbstractCursor<WriteCursor> {
    private int size;

    public WriteCursor(FieldDescriptor fieldDescriptor, WriteCursor parentCursor) {
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
