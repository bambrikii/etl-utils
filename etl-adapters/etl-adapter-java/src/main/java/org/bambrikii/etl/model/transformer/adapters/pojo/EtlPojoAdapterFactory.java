package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoResultSet;

public class EtlPojoAdapterFactory {
    private EtlPojoAdapterFactory() {
    }

    public static EtlPojoFieldReaderStrategy createPojoFieldReader() {
        return new EtlPojoFieldReaderStrategy();
    }

    public static EtlPojoFieldWriterStrategy createPojoFieldWriter() {
        return new EtlPojoFieldWriterStrategy();
    }

    public static EtlModelInputFactory<PojoResultSet> createPojoInputAdapter(Object data) {
        return new EtlPojoInputFactory(data);
    }

    public static EtlPojoOutputFactory createPojoOutputAdapter() {
        return new EtlPojoOutputFactory();
    }

    public static EtlPojoOutputFactory createPojoOutputAdapter(Object target) {
        return new EtlPojoOutputFactory(target);
    }
}
