package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambrikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambrikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambrikii.etl.model.transformer.builders.EtlNamable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.bambrikii.etl.model.transformer.adapters.db.EtlDbModelReader.ETL_DB_NAME;

public class EtlDbFieldReader extends EtlFieldReader<ResultSet> implements EtlNamable {
    private final Map<String, Integer> columnPositions;
    private final String name;

    public EtlDbFieldReader() {
        this(ETL_DB_NAME);
    }

    public EtlDbFieldReader(String name) {
        columnPositions = new HashMap<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private Integer getCachedColumnPosition(ResultSet obj, String name) throws SQLException {
        if (columnPositions.containsKey(name)) {
            return columnPositions.get(name);
        }
        int columnPosition = obj.findColumn(name);
        columnPositions.put(name, columnPosition);
        return columnPosition;
    }

    @Override
    protected EtlFieldExtractable<ResultSet, String> getStringReader(String name) {
        return obj -> {
            try {
                return obj.getString(getCachedColumnPosition(obj, name));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected EtlFieldExtractable<ResultSet, Integer> getIntReader(String name) {
        return obj -> {
            try {
                return obj.getInt(getCachedColumnPosition(obj, name));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected EtlFieldExtractable<ResultSet, Double> getDoubleReader(String name) {
        return obj -> {
            try {
                return obj.getDouble(getCachedColumnPosition(obj, name));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected EtlFieldExtractable<ResultSet, Map<String, Object>> getMapReader(String name) {
        return obj -> {
            try {
                Map<String, Object> result = new HashMap<>();
                result.put(name, obj.getObject(name));
                return result;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
