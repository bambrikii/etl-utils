package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtResultSet;

import static org.bambrikii.etl.model.transformer.adapers.swiftmt.EtlSwiftMtModelReader.ETL_SWIFT_MT_NAME;

public class EtlSwiftMtFieldReader extends EtlFieldReader<SwiftMtResultSet> implements EtlNamable {
    @Override
    public String getName() {
        return ETL_SWIFT_MT_NAME;
    }

    @Override
    protected EtlFieldExtractable<SwiftMtResultSet, String> getStringReader(String name) {
        return resultSet -> resultSet.getObject(name, String.class);
    }

    @Override
    protected EtlFieldExtractable<SwiftMtResultSet, Integer> getIntReader(String name) {
        return resultSet -> resultSet.getObject(name, Integer.class);
    }

    @Override
    protected EtlFieldExtractable<SwiftMtResultSet, Double> getDoubleReader(String name) {
        return resultSet -> resultSet.getObject(name, Double.class);
    }
}
