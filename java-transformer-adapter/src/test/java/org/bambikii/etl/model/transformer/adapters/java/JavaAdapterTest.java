package org.bambikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.ModelFieldAdapter;
import org.bambrikii.etl.model.transformer.adapters.java.JavaMapFactory;
import org.bambrikii.etl.model.transformer.adapters.java.JavaReflectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.bambikii.etl.model.transformer.adapters.java.AdapterTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaAdapterTest {
    @Test
    public void shouldBuildJavaMap() {
        ModelFieldAdapter adapter = createTestAdapter(
                JavaMapFactory.fieldReader(),
                JavaMapFactory.fieldWriter()
        );

        Map<String, Object> source = createTestInMap();
        Map<String, Object> target = createTestOutMap();

        adapter.adapt(source, target);

        assertEquals("str1", target.get("field1"));
        assertEquals(2, target.get("field2"));
    }

    @Test
    public void shouldBuildJavaReflection() {
        ModelFieldAdapter adapter = createTestAdapter(
                JavaReflectionFactory.fieldReader(TestInClass.class),
                JavaReflectionFactory.fieldWriter(TestOutClass.class)
        );

        TestInClass source = createTestInClass();
        TestOutClass target = createTestOutClass();

        adapter.adapt(source, target);

        assertEquals("str1", target.getField1());
        assertEquals(Integer.valueOf(2), target.getField2());
    }
}
