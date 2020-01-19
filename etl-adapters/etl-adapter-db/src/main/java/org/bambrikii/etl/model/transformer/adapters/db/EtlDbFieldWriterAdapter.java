package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EtlDbFieldWriterAdapter extends EtlFieldWriterStrategy<PreparedStatement> {
    private int paramIndex = 1;

    @Override
    protected EtlFieldLoadable<PreparedStatement, String> getStringWriter(String name) {
        return (obj, value) -> {
            try {
                obj.setString(paramIndex++, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected EtlFieldLoadable<PreparedStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> {
            try {
                obj.setInt(paramIndex++, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    @Override
    protected EtlFieldLoadable<PreparedStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> {
            try {
                obj.setDouble(paramIndex++, value);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        };
    }
}
