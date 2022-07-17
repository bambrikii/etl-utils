package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.bambrikii.etl.model.transformer.cursors.AbstractCursorsContainer;

public class PojoWriteCursorsContainer extends AbstractCursorsContainer<String, PojoWriteCursor, PojoFieldDescriptor> {
    @Override
    protected PojoWriteCursor createCursor(PojoFieldDescriptor fieldDescriptor, int size, PojoWriteCursor parentCursor) {
        return new PojoWriteCursor(fieldDescriptor, parentCursor);
    }
}
