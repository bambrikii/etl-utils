package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambrikii.etl.model.transformer.adapters.EtlModelWriter;
import org.bambrikii.etl.model.transformer.builders.EtlFieldWriter;
import org.bambrikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtStatement;

import static org.bambrikii.etl.model.transformer.adapers.swiftmt.EtlSwiftMtModelReader.ETL_SWIFT_MT_NAME;

public class EtlSwiftMtModelWriter implements EtlModelWriter<SwiftMtStatement>, EtlNamable {
    @Override
    public SwiftMtStatement create() {
        return new SwiftMtStatement();
    }

    @Override
    public boolean complete(SwiftMtStatement statement) {
        return false;
    }

    @Override
    public EtlFieldWriter<SwiftMtStatement> createFieldWriter() {
        return new EtlSwiftMtFieldWriter();
    }


    @Override
    public String getName() {
        return ETL_SWIFT_MT_NAME;
    }
}
