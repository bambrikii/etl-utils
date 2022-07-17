package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambrikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambrikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambrikii.etl.model.transformer.builders.EtlNamable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlDbModelReader implements EtlModelReader<ResultSet>, EtlNamable {
    public static final String ETL_DB_NAME = "db";

    private final String selectQuery;
    private final Connection cn;

    public EtlDbModelReader(Connection cn, String selectQuery) {
        this.cn = cn;
        this.selectQuery = selectQuery;
    }

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

    @Override
    public EtlFieldReader<ResultSet> createFieldReader() {
        return new EtlDbFieldReader();
    }

    @Override
    public String getName() {
        return ETL_DB_NAME;
    }
}
