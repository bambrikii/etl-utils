package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;

public class EtlSwiftMtFieldWriterStrategy extends EtlFieldWriterStrategy<EtlSwiftMtStatement> implements EtlNamable {
    public static final String ETL_SWIFT_MT_NAME = "swiftmt";

    @Override
    public String getName() {
        return ETL_SWIFT_MT_NAME;
    }

    @Override
    protected EtlFieldLoadable<EtlSwiftMtStatement, String> getStringWriter(String name) {
        return (obj, value) -> obj.setObject(name, (Object) value, String.class);
    }

    @Override
    protected EtlFieldLoadable<EtlSwiftMtStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> obj.setObject(name, (Object) value, Integer.class);
    }

    @Override
    protected EtlFieldLoadable<EtlSwiftMtStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> obj.setObject(name, (Object) value, Double.class);
    }
}
