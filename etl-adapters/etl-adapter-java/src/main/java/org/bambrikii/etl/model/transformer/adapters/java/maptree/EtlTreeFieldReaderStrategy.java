package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.io.EtlTreeResultSet;

public class EtlTreeFieldReaderStrategy extends EtlFieldReaderStrategy<EtlTreeResultSet> implements EtlNamable {
    public static final String ETL_JAVA_MAP_TREE_NAME = "java-map-tree";

    @Override
    public String getName() {
        return ETL_JAVA_MAP_TREE_NAME;
    }

    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, String> getStringReader(String name) {
        return resultSet -> resultSet.getObject(name, String.class);
    }

    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, Integer> getIntReader(String name) {
        return resultSet -> resultSet.getObject(name, Integer.class);
    }

    @Override
    protected EtlFieldExtractable<EtlTreeResultSet, Double> getDoubleReader(String name) {
        return resultSet -> resultSet.getObject(name, Double.class);
    }
}
