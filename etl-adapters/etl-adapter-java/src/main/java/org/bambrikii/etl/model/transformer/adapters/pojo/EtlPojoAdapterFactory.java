package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoResultSet;

public class EtlPojoAdapterFactory {
    private EtlPojoAdapterFactory() {
    }

    public static EtlPojoFieldReader createPojoFieldReader() {
        return new EtlPojoFieldReader();
    }

    public static EtlPojoFieldWriter createPojoFieldWriter() {
        return new EtlPojoFieldWriter();
    }

    public static EtlModelReader<PojoResultSet> createPojoReader(Object data) {
        return new EtlPojoModelReader(data);
    }

    public static EtlPojoModelWriter createPojoWriter() {
        return new EtlPojoModelWriter();
    }

    public static EtlPojoModelWriter createPojoWriter(Object target) {
        return new EtlPojoModelWriter(target);
    }
}
