package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeResultSet;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeStatement;

import java.util.Map;

public class EtlTreeAdapterFactory {
    private EtlTreeAdapterFactory() {
    }

    public static EtlTreeFieldReaderStrategy createTreeFieldReader() {
        return new EtlTreeFieldReaderStrategy();
    }

    public static EtlTreeFieldWriterStrategy createTreeFieldWriter() {
        return new EtlTreeFieldWriterStrategy();
    }

    public static EtlModelInputFactory<EtlTreeResultSet> createTreeInputAdapter(Map<String, Object> data) {
        return new EtlTreeInputFactory(data);
    }

    public static EtlModelOutputFactory<EtlTreeStatement> createTreeOutputAdapter() {
        return new EtlTreeOutputFactory();
    }
}
