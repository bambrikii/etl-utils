package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeResultSet;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeStatement;

import java.util.Map;

public class EtlTreeFactory {
    private EtlTreeFactory() {
    }

    public static EtlTreeFieldReaderStrategy jsonFieldReaderStrategy() {
        return new EtlTreeFieldReaderStrategy();
    }

    public static EtlTreeFieldWriterStrategy jsonFieldWriterStrategy() {
        return new EtlTreeFieldWriterStrategy();
    }

    public static EtlModelInputFactory<EtlTreeResultSet> jsonInput(Map<String, Object> data) {
        return new EtlTreeInputFactory(data);
    }

    public static EtlModelOutputFactory<EtlTreeStatement> jsonOutput() {
        return new EtlTreeOutputFactory();
    }
}
