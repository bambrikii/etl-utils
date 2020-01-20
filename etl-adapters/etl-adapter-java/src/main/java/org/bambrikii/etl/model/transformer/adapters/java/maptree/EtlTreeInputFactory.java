package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;

public class EtlTreeInputFactory implements EtlModelOutputFactory<EtlTreeStatement> {
    @Override
    public EtlTreeStatement create() {
        return new EtlTreeStatement();
    }

    @Override
    public boolean complete(EtlTreeStatement statement) {
        return false;
    }
}
