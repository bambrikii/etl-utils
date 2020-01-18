package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.ModelFieldAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelInputAdapter;
import org.bambikii.etl.model.transformer.builders.ConverterBuilder;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.bambrikii.etl.model.transformer.adapters.db.DbModelAdapterFactory.createDbInputAdapter;
import static org.bambrikii.etl.model.transformer.adapters.db.DbModelAdapterFactory.createDbOutputAdapter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DbAdapterTest {
    @Test
    public void shouldReadAndWrite() throws SQLException, JAXBException {
        ConverterBuilder builder = new ConverterBuilder();
        Map<String, ModelFieldAdapter> adapters = builder
                .readerStrategy("h2", DbAdapterFactory.fieldReader())
                .writerStrategy("h2", DbAdapterFactory.fieldWriter())
                .modelConfig(DbAdapterTest.class.getResourceAsStream("/model-config.xml"))
                .converterConfig(DbAdapterTest.class.getResourceAsStream("/converter-config.xml"))
                .build();

        try (Connection cn = DriverManager.getConnection("jdbc:h2:mem:");
             Statement statement1 = cn.createStatement();
             Statement statement2 = cn.createStatement();
        ) {
            cn.setAutoCommit(true);
            statement1.execute("create table t1 (col1 varchar2(256), col2 number(11, 0))");
            statement2.execute("create table t2 (col1_2 varchar2(256), col2_2 number(11, 0))");
            try (Statement statement3 = cn.createStatement()) {
                statement3.execute("insert into t1 (col1, col2) values ('str1', 3)");
            }

            ModelInputAdapter.adapt(
                    adapters.get("conversion1"),
                    createDbInputAdapter(cn, "select col1, col2 from t1"),
                    createDbOutputAdapter(cn, "insert into t2 (col1_2, col2_2) values (?1, ?2)")
            );

            try (ResultSet rs = cn.createStatement().executeQuery("select col1_2, col2_2 from t2")) {
                if (rs.next()) {
                    do {
                        assertEquals("str1", rs.getString("col1_2"));
                        assertEquals(3, rs.getInt("col2_2"));
                    } while (rs.next());
                } else {
                    fail("No records found");
                }
            }
        }
    }
}
