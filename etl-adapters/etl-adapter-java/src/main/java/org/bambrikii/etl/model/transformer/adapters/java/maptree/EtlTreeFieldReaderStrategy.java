package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;

public class EtlTreeFieldReaderStrategy extends EtlFieldReaderStrategy<EtlTreeResultSet> {
    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, String> getStringReader(String name) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, Integer> getIntReader(String name) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, Double> getDoubleReader(String name) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
