package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeStatement;

public class EtlTreeFieldWriterStrategy extends EtlFieldWriterStrategy<EtlTreeStatement> {
    @Override
    protected EtlFieldLoadable<EtlTreeStatement, String> getStringWriter(String name) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected EtlFieldLoadable<EtlTreeStatement, Integer> getIntWriter(String name) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected EtlFieldLoadable<EtlTreeStatement, Double> getDoubleWriter(String name) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
