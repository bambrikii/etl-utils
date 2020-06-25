package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EtlDbInputFactory implements EtlModelInputFactory<ResultSet> {
    private final String selectQuery;
    private final Connection cn;

    public EtlDbInputFactory(Connection cn, String selectQuery) {
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
}
