package org.bambrikii.etl.model.transformer.adapters.java.maptree.io.cursors;

import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.FieldDescriptor;

public class WriteCursorsContainer extends AbstractCursorsContainer<WriteCursor> {
    @Override
    protected WriteCursor createCursor(FieldDescriptor fieldDescriptor, int size, WriteCursor parentCursor) {
        return new WriteCursor(fieldDescriptor, parentCursor);
    }
}
