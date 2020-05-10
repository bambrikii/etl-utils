package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

public class ReadCursor extends AbstractCursor<ReadCursor> {
    private final int size;

    public ReadCursor(FieldDescriptor fieldDescriptor, int size, ReadCursor parentCursor) {
        super(fieldDescriptor, parentCursor);
        this.size = size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
