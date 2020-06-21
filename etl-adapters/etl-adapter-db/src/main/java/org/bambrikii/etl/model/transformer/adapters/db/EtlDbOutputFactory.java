package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EtlDbOutputFactory implements EtlModelOutputFactory<PreparedStatement> {
    private final Connection cn;
    private final String insertQuery;

    public EtlDbOutputFactory(Connection cn, String insertQuery) {
        this.cn = cn;
        this.insertQuery = insertQuery;
    }

    @Override
    public PreparedStatement create() {
        try {
            return cn.prepareStatement(insertQuery);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create prepared statement [" + insertQuery + "]", ex);
        }
    }

    @Override
    public boolean complete(PreparedStatement statement) {
        try {
            return statement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to complete prepared statement [" + insertQuery + "]", ex);
        }
    }
}
