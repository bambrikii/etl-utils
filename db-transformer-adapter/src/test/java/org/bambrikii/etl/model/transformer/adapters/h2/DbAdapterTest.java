package org.bambrikii.etl.model.transformer.adapters.h2;

import org.bambikii.etl.model.transformer.adapters.ModelAdapter;
import org.bambikii.etl.model.transformer.builders.ConverterBuilder;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DbAdapterTest {
    @Test
    public void shouldReadAndWrite() throws SQLException, JAXBException {
        ConverterBuilder builder = new ConverterBuilder();
        Map<String, ModelAdapter> adapter = builder
                .readerStrategy("h2", DbAdapterFactory.fieldReader())
                .writerStrategy("h2", DbAdapterFactory.fieldWriter())
                .modelConfig(DbAdapterTest.class.getResourceAsStream("/model-config.xml"))
                .converterConfig(DbAdapterTest.class.getResourceAsStream("/converter-config.xml"))
                .build();

        String url = "jdbc:h2:mem:";
        try (Connection cn = DriverManager.getConnection(url);
             Statement statement1 = cn.createStatement();
             Statement statement2 = cn.createStatement();
        ) {
            cn.setAutoCommit(true);
            statement1.execute("create table t1 (col1 varchar2(256), col2 number(11, 0))");
            statement2.execute("create table t2 (col1_2 varchar2(256), col2_2 number(11, 0))");
            try (Statement statement3 = cn.createStatement()) {
                statement3.execute("insert into t1 (col1, col2) values ('str1', 3)");
            }

            try (ResultSet rs = cn.createStatement().executeQuery("select col1, col2 from t1");
                 PreparedStatement statement5 = cn.prepareStatement("insert into t2 (col1_2, col2_2) values (?1, ?2)");
            ) {
                while (rs.next()) {
                    adapter.get("conversion1").adapt(rs, statement5);
                    statement5.execute();
                }
            }

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
