package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambrikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambrikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambrikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoResultSet;

import java.util.Map;

import static org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoModelReader.ETL_POJO;

public class EtlPojoFieldReader extends EtlFieldReader<PojoResultSet> implements EtlNamable {
    @Override
    public String getName() {
        return ETL_POJO;
    }

    @Override
    protected EtlFieldExtractable<PojoResultSet, String> getStringReader(String name) {
        return resultSet -> resultSet.getObject(name, String.class);
    }

    @Override
    protected EtlFieldExtractable<PojoResultSet, Integer> getIntReader(String name) {
        return resultSet -> resultSet.getObject(name, Integer.class);
    }

    @Override
    protected EtlFieldExtractable<PojoResultSet, Double> getDoubleReader(String name) {
        return resultSet -> resultSet.getObject(name, Double.class);
    }

    @Override
    protected EtlFieldExtractable<PojoResultSet, Map<String, Object>> getMapReader(String name) {
        return resultSet -> resultSet.getObject(name, Map.class);
    }
}
