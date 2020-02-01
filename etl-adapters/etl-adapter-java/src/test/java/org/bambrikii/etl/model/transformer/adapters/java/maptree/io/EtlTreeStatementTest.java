package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlTreeStatementTest {
    @Test
    public void shouldWriteToStatement() {
        Map<String, Object> data = new HashMap<>();

        TestClass1 testClass1 = new TestClass1();
        data.put("classField3", testClass1);

        List list = new ArrayList();
        data.put("listField5", list);

        EtlTreeStatement statement = new EtlTreeStatement(data);

        statement.setField("stringField1", "stringField1Value");
        statement.setField("intField2", Integer.valueOf(2));
        statement.setField("classField3<org.bambrikii.etl.model.transformer.adapters.java.maptree.io.TestClass1>.stringField4", "stringField4Value");

        statement.setField("listField5.", "listField5StringElement6");
        statement.setField("listField5.", Integer.valueOf(7));

        statement.setField("listField6.", "listField6[].[].", "6");
        statement.setField("listField6.", "listField6[].[].", "7");


        assertEquals("stringField1Value", data.get("stringField1"));
        assertEquals(Integer.valueOf(2), data.get("intField2"));

        TestClass1 classField3 = (TestClass1) data.get("classField3");
        assertEquals("stringField4Value", classField3.getStringField4());

        List listField5 = (List) data.get("listField5");
        assertEquals(2, listField5.size());
        assertEquals("listField5StringElement6", listField5.get(0));
        assertEquals(Integer.valueOf(7), listField5.get(1));

        List listField6 = (List) data.get("listField6");
        assertEquals(2, listField6.size());

        List listField6_1 = (List) listField6.get(0);
        assertEquals(1, listField6_1.size());
        assertEquals("6", listField6_1.get(0));

        List listField6_2 = (List) listField6.get(1);
        assertEquals(1, listField6_2.size());
        assertEquals("7", listField6_2.get(0));
    }

    @Test
    public void shouldSetNestedList() {
        Map<String, Object> data = new HashMap<>();

        EtlTreeStatement statement = new EtlTreeStatement(data);

        statement.setField("listField6.", "listField6[].[].", "6");
        statement.setField("listField6.", "listField6[].[].", "7");

        List listField6 = (List) data.get("listField6");
        assertEquals(2, listField6.size());

        List listField6_1 = (List) listField6.get(0);
        assertEquals(1, listField6_1.size());
        assertEquals("6", listField6_1.get(0));

        List listField6_2 = (List) listField6.get(1);
        assertEquals(1, listField6_2.size());
        assertEquals("7", listField6_2.get(0));
    }
}
