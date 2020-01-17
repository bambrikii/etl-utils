package org.bambrikii.etl.model.transformer.adapters.h2;

import org.bambikii.etl.model.transformer.adapters.ModelOutputAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbModelOutputAdapter implements ModelOutputAdapter<PreparedStatement> {
    private final Connection cn;
    private final String query;

    public DbModelOutputAdapter(Connection cn, String query) {
        this.cn = cn;
        this.query = query;
    }

    @Override
    public PreparedStatement next() {
        try {
            PreparedStatement ps = cn.prepareStatement(query);
            return ps;
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to build prepared statement for [" + query + "]", ex);
        }
    }
}
