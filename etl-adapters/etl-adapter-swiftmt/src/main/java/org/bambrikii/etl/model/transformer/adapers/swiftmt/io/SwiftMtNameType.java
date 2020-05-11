package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

enum SwiftMtNameType {
    SEQUENCE(null), BLOCK(SEQUENCE), TAG(BLOCK), QUALIFIER(TAG), COMPONENT(QUALIFIER);
    private SwiftMtNameType parentType;

    SwiftMtNameType(SwiftMtNameType parentType) {
        this.parentType = parentType;
    }

    public SwiftMtNameType parentType() {
        return parentType;
    }
}
