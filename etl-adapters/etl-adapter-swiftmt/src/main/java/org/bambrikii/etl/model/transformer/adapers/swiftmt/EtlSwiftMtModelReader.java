package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import org.bambikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtResultSet;

import java.io.IOException;

public class EtlSwiftMtModelReader implements EtlModelReader<SwiftMtResultSet>, EtlNamable {
    public static final String ETL_SWIFT_MT_NAME = "swift-mt";

    private final String content;
    private boolean next;

    public EtlSwiftMtModelReader(String content) {
        this.content = content;
    }

    @Override
    public SwiftMtResultSet create() {
        try {
            AbstractMT message = SwiftMessage.parse(content).toMT();
            next = true;
            return new SwiftMtResultSet(message);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot parse Swift message: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean next(SwiftMtResultSet resultSet) {
        if (!next) {
            return false;
        }
        next = false;
        return true;
    }

    @Override
    public EtlFieldReader<SwiftMtResultSet> createFieldReader() {
        return new EtlSwiftMtFieldReader();
    }

    @Override
    public String getName() {
        return ETL_SWIFT_MT_NAME;
    }
}
