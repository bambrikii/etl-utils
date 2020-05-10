package org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets;

import org.bambrikii.etl.model.transformer.adapters.pojo.io.TestClass1;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PojoResultSetTest {
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

        PojoResultSet resultSet = new PojoResultSet(data);

        assertEquals("stringField1Value", resultSet.getObject("stringField1", String.class));
        assertEquals(Integer.valueOf(2), resultSet.getObject("intField2", Integer.class));

        assertEquals("stringField4Value", resultSet.getObject("classField3.stringField4", String.class));

        assertEquals("stringField6Value", resultSet.getObject("listField5..stringField6", String.class));
        if (!resultSet.next()) { // Advancing listField5
            fail();
        }

        assertEquals(Integer.valueOf(7), resultSet.getObject("listField5..intField7", Integer.class));
        if (resultSet.next()) { // Advancing listField5
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

        PojoResultSet resultSet = new PojoResultSet(data);

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
        if (resultSet.next()) {
            fail();
        }
    }

    @Test
    public void shouldReadNestedMaps() {
        Map<String, Object> map3 = new HashMap<>();
        map3.put("str1", "str1Value");

        Map<String, Object> map2 = new HashMap<>();
        map2.put("map2", map3);

        Map<String, Object> data = new HashMap<>();
        data.put("map1", map2);

        PojoResultSet resultSet = new PojoResultSet(data);

        assertEquals("str1Value", resultSet.getObject("map1.map2.str1", String.class));
        assertTrue(resultSet.isStatusValid());
    }

    @Test
    public void shouldFailRead() {
        Map<String, Object> data = new HashMap<>();
        data.put("list1", new ArrayList());

        PojoResultSet resultSet = new PojoResultSet(data);

        assertNull(resultSet.getObject("list1.", String.class));

        assertEquals(1, resultSet.statuses().size());
        assertFalse(resultSet.isStatusValid());
    }
}
