package org.bambrikii.etl.model.transformer.adapters.h2;

import org.bambikii.etl.model.transformer.adapters.ModelFieldAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelInputAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelOutputAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbModelInputAdapter implements ModelInputAdapter {
    private Connection cn;
    private String query;
    private ModelFieldAdapter modelFieldAdapter;
    private ModelOutputAdapter outputStream;

    public DbModelInputAdapter(Connection cn, String query, ModelFieldAdapter modelFieldAdapter, ModelOutputAdapter modelOutputAdapter) {
        this.cn = cn;
        this.query = query;
        this.modelFieldAdapter = modelFieldAdapter;
        this.outputStream = modelOutputAdapter;
    }

    @Override
    public void adapt() {
        try (Statement statement = createStatement();
             ResultSet rs = statement.executeQuery(query)
        ) {
            while (rs.next()) {
                modelFieldAdapter.adapt(rs, outputStream.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Statement createStatement() throws SQLException {
        return cn.createStatement();
    }
}
