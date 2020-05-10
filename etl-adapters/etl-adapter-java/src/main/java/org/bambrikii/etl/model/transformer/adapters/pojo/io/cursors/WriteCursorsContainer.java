package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

public class WriteCursorsContainer extends AbstractCursorsContainer<WriteCursor> {
    @Override
    protected WriteCursor createCursor(FieldDescriptor fieldDescriptor, int size, WriteCursor parentCursor) {
        return new WriteCursor(fieldDescriptor, parentCursor);
    }
}
