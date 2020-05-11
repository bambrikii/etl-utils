package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import org.bambikii.etl.model.transformer.cursors.AbstractCursorsContainer;

public class SwiftMtCursorsContainer extends AbstractCursorsContainer<
        SwiftMtNameElement,
        SwiftMtCursor,
        SwiftMtFieldDescriptor
        > {
    @Override
    protected SwiftMtCursor createCursor(SwiftMtFieldDescriptor fieldDescriptor, int size, SwiftMtCursor parentCursor) {
        return new SwiftMtCursor(fieldDescriptor, parentCursor, size);
    }
}
