package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoResultSet;

public class EtlPojoInputFactory implements EtlModelInputFactory<PojoResultSet> {
    private final Object data;
    private boolean next;

    public EtlPojoInputFactory(Object data) {
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
}
