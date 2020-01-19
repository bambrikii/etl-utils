package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlDbAdapterFactory {
    private EtlDbAdapterFactory() {
    }


    public static EtlFieldReaderStrategy<ResultSet> createDbFieldReader() {
        return new EtlDbFieldReaderAdapter();
    }

    public static EtlFieldWriterStrategy<PreparedStatement> createDbFieldWriter() {
        return new EtlDbFieldWriterAdapter();
    }


    public static EtlModelInputFactory<ResultSet> createDbInputAdapter(Connection cn, String selectQuery) {
        return new EtlModelInputFactory<ResultSet>() {
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

    public static EtlModelOutputFactory<PreparedStatement> createDbOutputAdapter(Connection cn, String insertQuery) {
        return new EtlModelOutputFactory<PreparedStatement>() {
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
