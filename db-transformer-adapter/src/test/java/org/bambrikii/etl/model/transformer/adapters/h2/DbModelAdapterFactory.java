package org.bambrikii.etl.model.transformer.adapters.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DbModelAdapterFactory {

    public static final Predicate<ResultSet> RESULT_SET_ADVANCE_PREDICATE = resultSet -> {
        try {
            return resultSet.next();
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to advance ResultSet.", ex);
        }
    };

    private DbModelAdapterFactory() {
    }

    public static Supplier<ResultSet> createInputAdapter(Connection cn, String selectQuery) {
        return () -> {
            try {
                return cn.prepareStatement(selectQuery).executeQuery();
            } catch (SQLException ex) {
                throw new RuntimeException("Failed to execute query[" + selectQuery + "]", ex);
            }
        };
    }

    public static Predicate<ResultSet> createInputAdvance() {
        return RESULT_SET_ADVANCE_PREDICATE;
    }

    public static Supplier<PreparedStatement> createOutputAdapter(Connection cn, String insertQuery) {
        return () -> {
            try {
                return cn.prepareStatement(insertQuery);
            } catch (SQLException ex) {
                throw new RuntimeException("Failed to create prepared statement [" + insertQuery + "]", ex);
            }
        };
    }
}
