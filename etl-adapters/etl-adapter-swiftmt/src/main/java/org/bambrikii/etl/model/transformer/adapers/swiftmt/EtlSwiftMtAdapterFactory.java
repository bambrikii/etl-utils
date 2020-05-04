package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;

public class EtlSwiftMtAdapterFactory {
    private EtlSwiftMtAdapterFactory() {
    }

    public static EtlSwiftMtFieldReaderStrategy createSwiftMtFieldReader() {
        return new EtlSwiftMtFieldReaderStrategy();
    }

    public static EtlSwiftMtFieldWriterStrategy createSwiftMtFieldWriter() {
        return new EtlSwiftMtFieldWriterStrategy();
    }

    public static EtlModelInputFactory<EtlSwiftMtResultSet> createSwiftMtInputAdapter(String content) {
        return new EtlSwiftMtInputFactory(content);
    }

    public static EtlModelOutputFactory<EtlSwiftMtStatement> createSwiftMtOutputAdapter() {
        return new EtlSwiftMtOutputFactory();
    }
}
