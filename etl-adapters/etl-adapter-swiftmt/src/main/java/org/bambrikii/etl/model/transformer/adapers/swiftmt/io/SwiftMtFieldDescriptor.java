package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import org.bambikii.etl.model.transformer.cursors.AbstractFieldDescriptor;

public class SwiftMtFieldDescriptor extends AbstractFieldDescriptor<SwiftMtNameElement, SwiftMtFieldDescriptor> {
    public SwiftMtFieldDescriptor(SwiftMtNameElement nameElement, boolean array, SwiftMtFieldDescriptor parent) {
        super(nameElement, array, parent);
    }
}
