package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

public class ReadCursorsContainer extends AbstractCursorsContainer<ReadCursor> {
    @Override
    protected ReadCursor createCursor(FieldDescriptor fieldDescriptor, int size, ReadCursor parentCursor) {
        return new ReadCursor(fieldDescriptor, size, parentCursor);
    }
}
