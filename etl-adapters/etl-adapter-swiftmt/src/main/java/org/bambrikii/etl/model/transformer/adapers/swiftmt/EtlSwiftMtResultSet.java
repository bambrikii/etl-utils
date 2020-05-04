package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import com.prowidesoftware.swift.model.mt.AbstractMT;

public class EtlSwiftMtResultSet {
    private final AbstractMT message;

    public EtlSwiftMtResultSet(AbstractMT message) {
        this.message = message;
    }

    public <T> T getObject(String name, Class<T> cls) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
