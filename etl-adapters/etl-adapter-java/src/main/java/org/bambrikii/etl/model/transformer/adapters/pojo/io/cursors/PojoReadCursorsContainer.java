package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.bambrikii.etl.model.transformer.cursors.AbstractCursorsContainer;

public class PojoReadCursorsContainer extends AbstractCursorsContainer<String, PojoReadCursor, PojoFieldDescriptor> {
    @Override
    protected PojoReadCursor createCursor(PojoFieldDescriptor fieldDescriptor, int size, PojoReadCursor parentCursor) {
        return new PojoReadCursor(fieldDescriptor, size, parentCursor);
    }
}
