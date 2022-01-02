package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoResultSet;

public class EtlPojoModelReader implements EtlModelReader<PojoResultSet>, EtlNamable {
    public static final String ETL_POJO = "pojo";

    private final Object data;
    private boolean next;

    public EtlPojoModelReader(Object data) {
        this.data = data;
        this.next = true;
    }

    @Override
    public PojoResultSet create() {
        return new PojoResultSet(data);
    }

    @Override
    public boolean next(PojoResultSet resultSet) {
        if (next) {
            next = false;
            return true;
        }
        return resultSet.next();
    }

    @Override
    public EtlFieldReader<PojoResultSet> createFieldReader() {
        return new EtlPojoFieldReader();
    }

    @Override
    public String getName() {
        return ETL_POJO;
    }
}
