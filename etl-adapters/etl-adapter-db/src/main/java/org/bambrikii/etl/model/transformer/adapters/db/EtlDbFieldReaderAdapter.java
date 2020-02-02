package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlFieldExtractable;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EtlDbFieldReaderAdapter extends EtlFieldReaderStrategy<ResultSet> implements EtlNamable {
    public static final String ETL_DB_NAME = "db";

    private final Map<String, Integer> columnPositions;
    private final String name;

    public EtlDbFieldReaderAdapter() {
        this(ETL_DB_NAME);
    }

    public EtlDbFieldReaderAdapter(String name) {
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
}
