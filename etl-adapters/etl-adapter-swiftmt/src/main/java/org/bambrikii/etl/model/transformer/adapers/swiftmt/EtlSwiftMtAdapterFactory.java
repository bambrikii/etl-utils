package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtResultSet;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtStatement;

public class EtlSwiftMtAdapterFactory {
    private EtlSwiftMtAdapterFactory() {
    }

    public static EtlSwiftMtFieldReaderStrategy createSwiftMtFieldReader() {
        return new EtlSwiftMtFieldReaderStrategy();
    }

    public static EtlSwiftMtFieldWriterStrategy createSwiftMtFieldWriter() {
        return new EtlSwiftMtFieldWriterStrategy();
    }

    public static EtlModelInputFactory<SwiftMtResultSet> createSwiftMtInputAdapter(String content) {
        return new EtlSwiftMtInputFactory(content);
    }

    public static EtlModelOutputFactory<SwiftMtStatement> createSwiftMtOutputAdapter() {
        return new EtlSwiftMtOutputFactory();
    }
}
