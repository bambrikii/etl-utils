package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbFieldWriterAdapter extends FieldWriterStrategy<PreparedStatement> {
    private int paramIndex = 1;

    @Override
    protected FieldWriterAdapter<PreparedStatement, String> getStringWriter(String name) {
        return (obj, value) -> {
            try {
                obj.setString(paramIndex++, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldWriterAdapter<PreparedStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> {
            try {
                obj.setInt(paramIndex++, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldWriterAdapter<PreparedStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> {
            try {
                obj.setDouble(paramIndex++, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
