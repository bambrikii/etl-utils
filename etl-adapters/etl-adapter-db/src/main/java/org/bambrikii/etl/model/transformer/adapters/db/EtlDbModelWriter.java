package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlModelWriter;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriter;
import org.bambikii.etl.model.transformer.builders.EtlNamable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.bambrikii.etl.model.transformer.adapters.db.EtlDbModelReader.ETL_DB_NAME;

public class EtlDbModelWriter implements EtlModelWriter<PreparedStatement>, EtlNamable {
    private final Connection cn;
    private final String insertQuery;

    public EtlDbModelWriter(Connection cn, String insertQuery) {
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

    @Override
    public EtlFieldWriter<PreparedStatement> createFieldWriter() {
        return new EtlDbFieldWriter();
    }

    @Override
    public String getName() {
        return ETL_DB_NAME;
    }
}
