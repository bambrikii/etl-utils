package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriter;
import org.bambikii.etl.model.transformer.builders.EtlNamable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.bambrikii.etl.model.transformer.adapters.db.EtlDbModelReader.ETL_DB_NAME;

public class EtlDbFieldWriter extends EtlFieldWriter<PreparedStatement> implements EtlNamable {
    private final String name;
    private int paramIndex = 1;

    public EtlDbFieldWriter() {
        this(ETL_DB_NAME);
    }

    public EtlDbFieldWriter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
