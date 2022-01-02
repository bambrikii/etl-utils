package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambikii.etl.model.transformer.adapters.EtlModelWriter;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtResultSet;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtStatement;

public class EtlSwiftMtAdapterFactory {
    private EtlSwiftMtAdapterFactory() {
    }

    public static EtlModelReader<SwiftMtResultSet> createSwiftMtReader(String content) {
        return new EtlSwiftMtModelReader(content);
    }

    public static EtlModelWriter<SwiftMtStatement> createSwiftMtWriter() {
        return new EtlSwiftMtModelWriter();
    }
}
