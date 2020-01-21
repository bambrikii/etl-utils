package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeResultSet;

import java.util.Map;

public class EtlTreeInputFactory implements EtlModelInputFactory<EtlTreeResultSet> {
    private final Map<String, Object> data;

    public EtlTreeInputFactory(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public EtlTreeResultSet create() {
        return new EtlTreeResultSet(data);
    }

    @Override
    public boolean next(EtlTreeResultSet resultSet) {
        return resultSet.next();
    }
}
