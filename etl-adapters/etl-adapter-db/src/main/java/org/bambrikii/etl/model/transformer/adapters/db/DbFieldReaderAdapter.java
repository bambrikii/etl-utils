package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;
import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DbFieldReaderAdapter extends FieldReaderStrategy<ResultSet> {
    private final Map<String, Integer> columnPositions;

    public DbFieldReaderAdapter() {
        columnPositions = new HashMap<>();
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
    protected FieldReaderAdapter<ResultSet, String> getStringReader(String name) {
        return obj -> {
            try {
                return obj.getString(getCachedColumnPosition(obj, name));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldReaderAdapter<ResultSet, Integer> getIntReader(String name) {
        return obj -> {
            try {
                return obj.getInt(getCachedColumnPosition(obj, name));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldReaderAdapter<ResultSet, Double> getDoubleReader(String name) {
        return obj -> {
            try {
                return obj.getDouble(getCachedColumnPosition(obj, name));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
