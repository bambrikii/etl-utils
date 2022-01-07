package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriter;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtStatement;

import java.util.Map;

import static org.bambrikii.etl.model.transformer.adapers.swiftmt.EtlSwiftMtModelReader.ETL_SWIFT_MT_NAME;

public class EtlSwiftMtFieldWriter extends EtlFieldWriter<SwiftMtStatement> implements EtlNamable {
    @Override
    public String getName() {
        return ETL_SWIFT_MT_NAME;
    }

    @Override
    protected EtlFieldLoadable<SwiftMtStatement, String> getStringWriter(String name) {
        return (obj, value) -> obj.setObject(name, value, String.class);
    }

    @Override
    protected EtlFieldLoadable<SwiftMtStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> obj.setObject(name, value, Integer.class);
    }

    @Override
    protected EtlFieldLoadable<SwiftMtStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> obj.setObject(name, value, Double.class);
    }

    @Override
    protected EtlFieldLoadable<SwiftMtStatement, Map<String, Object>> getMapWriter(String name) {
        throw new UnsupportedOperationException("Not supported");
    }
}
