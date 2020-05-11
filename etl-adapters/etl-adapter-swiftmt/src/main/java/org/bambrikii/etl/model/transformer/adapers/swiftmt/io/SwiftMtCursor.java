package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import org.bambikii.etl.model.transformer.cursors.AbstractCursor;

public class SwiftMtCursor extends AbstractCursor<SwiftMtNameElement, SwiftMtCursor, SwiftMtFieldDescriptor> {
    private final int size;

    public SwiftMtCursor(SwiftMtFieldDescriptor fieldDescriptor, SwiftMtCursor parentCursor, int size) {
        super(fieldDescriptor, parentCursor);
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
