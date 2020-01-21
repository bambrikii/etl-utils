package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeResultSet;

public class EtlTreeFieldReaderStrategy extends EtlFieldReaderStrategy<EtlTreeResultSet> {
    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, String> getStringReader(String name) {
        return resultSet -> resultSet.getString(name);
    }

    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, Integer> getIntReader(String name) {
        return resultSet -> resultSet.getInteger(name);
    }

    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, Double> getDoubleReader(String name) {
        return resultSet -> resultSet.getDouble(name);
    }
}
