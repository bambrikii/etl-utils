package org.bambrikii.etl.model.transformer.adapters.h2;

import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;
import org.bambikii.etl.model.transformer.builders.FieldWriterFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class H2FieldWriterAdapter extends FieldWriterFactory<PreparedStatement> {
    @Override
    protected FieldWriterAdapter<PreparedStatement, String> getStringWriter(String name) {
        return (obj, value) -> {
            int pos = -1;
            try {
                obj.setString(pos, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldWriterAdapter<PreparedStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> {
            int pos = -1;
            try {
                obj.setInt(pos, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected FieldWriterAdapter<PreparedStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> {
            int pos = -1;
            try {
                obj.setDouble(pos, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
