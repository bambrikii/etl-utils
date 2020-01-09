package org.bambrikii.etl.model.transformer.adapters.h2;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;
import org.bambikii.etl.model.transformer.builders.FieldReaderFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class H2FieldReaderAdapter<T> extends FieldReaderFactory<ResultSet> {
    @Override
    protected FieldReaderAdapter<ResultSet, String> getStringReader(String name) {
        return obj -> {
            try {
                return obj.getString(name);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldReaderAdapter<ResultSet, Integer> getIntReader(String name) {
        return obj -> {
            try {
                return obj.getInt(name);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldReaderAdapter<ResultSet, Double> getDoubleReader(String name) {
        return obj -> {
            try {
                return obj.getDouble(name);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
