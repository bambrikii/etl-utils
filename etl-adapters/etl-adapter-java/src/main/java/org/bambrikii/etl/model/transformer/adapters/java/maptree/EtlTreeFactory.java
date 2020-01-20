package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;

public class EtlTreeFactory {
    private EtlTreeFactory() {
    }

    public static EtlTreeFieldReaderStrategy jsonFieldReaderStrategy() {
        return new EtlTreeFieldReaderStrategy();
    }

    public static EtlTreeFieldWriterStrategy jsonFieldWriterStrategy() {
        return new EtlTreeFieldWriterStrategy();
    }

    public static EtlModelInputFactory<EtlTreeResultSet> jsonInput() {
        return new EtlTreeOutputFactory();
    }

    public static EtlModelOutputFactory<EtlTreeStatement> jsonOutput() {
        return new EtlTreeInputFactory();
    }
}
