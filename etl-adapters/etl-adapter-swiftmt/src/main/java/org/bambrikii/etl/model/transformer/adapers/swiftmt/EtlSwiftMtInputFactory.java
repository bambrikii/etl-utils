package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;

import java.io.IOException;

public class EtlSwiftMtInputFactory implements EtlModelInputFactory<EtlSwiftMtResultSet> {
    private final String content;
    private boolean next;

    public EtlSwiftMtInputFactory(String content) {
        this.content = content;
    }

    @Override
    public EtlSwiftMtResultSet create() {
        try {
            AbstractMT message = SwiftMessage.parse(content).toMT();
            next = true;
            return new EtlSwiftMtResultSet(message);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot parse Swift message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean next(EtlSwiftMtResultSet resultSet) {
        if (!next) {
            return false;
        }
        next = false;
        return true;
    }
}
