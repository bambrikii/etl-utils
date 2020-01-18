package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.ModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.ModelOutputFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbModelAdapterFactory {
    private DbModelAdapterFactory() {
    }

    public static ModelInputFactory<ResultSet> createDbInputAdapter(Connection cn, String selectQuery) {
        return new ModelInputFactory<ResultSet>() {
            @Override
            public ResultSet create() {
                try {
                    return cn.prepareStatement(selectQuery).executeQuery();
                } catch (SQLException ex) {
                    throw new RuntimeException("Failed to execute query[" + selectQuery + "]", ex);
                }
            }

            @Override
            public boolean next(ResultSet resultSet) {
                try {
                    return resultSet.next();
                } catch (SQLException ex) {
                    throw new RuntimeException("Failed to advance ResultSet.", ex);
                }
            }
        };
    }

    public static ModelOutputFactory<PreparedStatement> createDbOutputAdapter(Connection cn, String insertQuery) {
        return new ModelOutputFactory<PreparedStatement>() {
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
        };
    }
}
