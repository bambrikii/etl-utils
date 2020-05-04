package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;

public class EtlSwiftMtOutputFactory implements EtlModelOutputFactory<EtlSwiftMtStatement> {
    @Override
    public EtlSwiftMtStatement create() {
        return new EtlSwiftMtStatement();
    }

    @Override
    public boolean complete(EtlSwiftMtStatement statement) {
        return false;
    }
}
