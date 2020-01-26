package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EtlTreeResultSetTest {
    @Test
    public void shouldReadFromResultSet() {
        Map<String, Object> data = new HashMap<>();
        data.put("stringField1", "stringField1Value");
        data.put("intField2", 2);

        TestClass1 testClass1 = new TestClass1();
        testClass1.setStringField4("stringField4Value");
        data.put("classField3", testClass1);

        List<Object> list5 = new ArrayList<>();
        Map<String, Object> map5 = new HashMap<>();
        map5.put("stringField6", "stringField6Value");
        list5.add(map5);

        Map<String, Object> map7 = new HashMap<>();
        map7.put("intField7", 7);
        list5.add(map7);

        data.put("listField5", list5);

        EtlTreeResultSet resultSet = new EtlTreeResultSet(data);

        if (resultSet.next()) {
            assertEquals("stringField1Value", resultSet.getObject("stringField1", String.class));
            assertEquals(Integer.valueOf(2), resultSet.getObject("intField2", Integer.class));

            assertEquals("stringField4Value", resultSet.getObject("classField3.stringField4", String.class));

            assertEquals("stringField6Value", resultSet.getObject("listField5..stringField6", String.class));
        } else {
            fail();
        }
        if (resultSet.next()) {
            assertEquals(Integer.valueOf(7), resultSet.getObject("listField5..intField7", Integer.class));
        } else {
            fail();
        }
    }

    @Test
    public void shouldReadNestedCursors() {
        Map<String, Object> data = new HashMap<>();

        List<Object> list1 = new ArrayList<>();

        Map<String, Object> map1 = new HashMap<>();
        List<String> list2 = new ArrayList<>();
        list2.add("str1");
        list2.add("str2");

        map1.put("list2", list2);
        list1.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        List<Object> list3 = new ArrayList<>();
        list3.add("str3");
        list3.add("str4");
        map2.put("list3", list3);
        list1.add(map2);

        data.put("list1", list1);

        EtlTreeResultSet resultSet = new EtlTreeResultSet(data);

        if (!resultSet.next()) {
            fail();
        }
        assertEquals("str1", resultSet.getObject("list1..list2.", String.class));

        if (!resultSet.next()) {
            fail();
        }
        assertEquals("str2", resultSet.getObject("list1..list2.", String.class));

        if (!resultSet.next()) {
            fail();
        }
        assertEquals("str3", resultSet.getObject("list1..list3.", String.class));

        if (!resultSet.next()) {
            fail();
        }
        assertEquals("str4", resultSet.getObject("list1..list3.", String.class));
    }

    @Test
    public void shouldReadNestedMaps() {
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        Map<String, Object> map3 = new HashMap<>();
        map3.put("str1", "str1Value");
        map2.put("map2", map3);
        data.put("map1", map2);

        EtlTreeResultSet resultSet = new EtlTreeResultSet(data);

        assertEquals("str1Value", resultSet.getObject("map1.map2.str1", String.class));
    }
}
