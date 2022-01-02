package org.bambrikii.etl.model.transformer.adapters.db;

import jakarta.xml.bind.JAXBException;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.builders.EtlAdapterConfigBuilder;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EtlDbAdapterTest {
    @Test
    public void shouldReadAndWrite() throws SQLException, JAXBException {
        EtlAdapterConfigBuilder builder = new EtlAdapterConfigBuilder();
        Map<String, EtlModelAdapter> adapters = builder.readerStrategy(EtlDbAdapterFactory.createDbFieldReader())
                .writerStrategy(EtlDbAdapterFactory.createDbFieldWriter())
                .modelConfig(EtlDbAdapterTest.class.getResourceAsStream("/model-config.xml"))
                .conversionConfig(EtlDbAdapterTest.class.getResourceAsStream("/mapping-config.xml")).buildMap();

        try (Connection cn = DriverManager.getConnection("jdbc:h2:mem:");
                Statement statement1 = cn.createStatement();
                Statement statement2 = cn.createStatement();) {
            cn.setAutoCommit(true);
            statement1.execute("create table t1 (col1 varchar2(256), col2 number(11, 0))");
            statement2.execute("create table t2 (col1_2 varchar2(256), col2_2 number(11, 0))");
            try (Statement statement3 = cn.createStatement()) {
                statement3.execute("insert into t1 (col1, col2) values ('str1', 3)");
            }

            adapters.get("conversion1").adapt(EtlDbAdapterFactory.createDbReader(cn, "select col1, col2 from t1"),
                    EtlDbAdapterFactory.createDbWriter(cn, "insert into t2 (col1_2, col2_2) values (?1, ?2)"));

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
