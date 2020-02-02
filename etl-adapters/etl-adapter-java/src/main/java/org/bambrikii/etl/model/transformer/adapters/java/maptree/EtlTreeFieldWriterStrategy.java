package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeStatement;

import static org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeFieldReaderStrategy.ETL_JAVA_MAP_TREE_NAME;

public class EtlTreeFieldWriterStrategy extends EtlFieldWriterStrategy<EtlTreeStatement> implements EtlNamable {
    @Override
    public String getName() {
        return ETL_JAVA_MAP_TREE_NAME;
    }

    @Override
    protected EtlFieldLoadable<EtlTreeStatement, String> getStringWriter(String name) {
        return (obj, value) -> obj.setField(name, (Object) value, String.class);
    }

    @Override
    protected EtlFieldLoadable<EtlTreeStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> obj.setField(name, value, Integer.class);
    }

    @Override
    protected EtlFieldLoadable<EtlTreeStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> obj.setField(name, value, Double.class);
    }
}
