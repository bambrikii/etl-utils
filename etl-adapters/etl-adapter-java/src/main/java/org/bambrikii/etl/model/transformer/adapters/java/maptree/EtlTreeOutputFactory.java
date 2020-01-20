package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;

public class EtlTreeOutputFactory implements EtlModelInputFactory<EtlTreeResultSet> {
    @Override
    public EtlTreeResultSet create() {
        return new EtlTreeResultSet();
    }

    @Override
    public boolean next(EtlTreeResultSet resultSet) {
        return false;
    }
}
