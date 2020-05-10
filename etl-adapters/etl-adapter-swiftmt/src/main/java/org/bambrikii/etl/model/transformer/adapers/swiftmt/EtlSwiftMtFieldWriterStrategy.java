package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtStatement;

public class EtlSwiftMtFieldWriterStrategy extends EtlFieldWriterStrategy<SwiftMtStatement> implements EtlNamable {
    public static final String ETL_SWIFT_MT_NAME = "swiftmt";

    @Override
    public String getName() {
        return ETL_SWIFT_MT_NAME;
    }

    @Override
    protected EtlFieldLoadable<SwiftMtStatement, String> getStringWriter(String name) {
        return (obj, value) -> obj.setObject(name, (Object) value, String.class);
    }

    @Override
    protected EtlFieldLoadable<SwiftMtStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> obj.setObject(name, (Object) value, Integer.class);
    }

    @Override
    protected EtlFieldLoadable<SwiftMtStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> obj.setObject(name, (Object) value, Double.class);
    }
}
