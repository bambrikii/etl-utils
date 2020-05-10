package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtStatement;

public class EtlSwiftMtOutputFactory implements EtlModelOutputFactory<SwiftMtStatement> {
    @Override
    public SwiftMtStatement create() {
        return new SwiftMtStatement();
    }

    @Override
    public boolean complete(SwiftMtStatement statement) {
        return false;
    }
}
