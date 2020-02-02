package org.bambrikii.etl.model.transformer.adapters.java.maptree.io.cursors;

import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.FieldDescriptor;

public class ReadCursorsContainer extends AbstractCursorsContainer<ReadCursor> {
    @Override
    protected ReadCursor createCursor(FieldDescriptor fieldDescriptor, int size, ReadCursor parentCursor) {
        return new ReadCursor(fieldDescriptor, size, parentCursor);
    }
}
