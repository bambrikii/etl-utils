package org.bambikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.EtlFieldAdapter;
import org.bambrikii.etl.model.transformer.adapters.java.EtlJavaMapFactory;
import org.bambrikii.etl.model.transformer.adapters.java.EtlJavaReflectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.bambikii.etl.model.transformer.adapters.java.EtlAdapterTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlJavaAdapterTest {
    @Test
    public void shouldBuildJavaMap() {
        EtlFieldAdapter adapter = createTestAdapter(
                EtlJavaMapFactory.fieldReader(),
                EtlJavaMapFactory.fieldWriter()
        );

        Map<String, Object> source = createTestInMap();
        Map<String, Object> target = createTestOutMap();

        adapter.adapt(source, target);

        assertEquals("str1", target.get("field1"));
        assertEquals(2, target.get("field2"));
    }

    @Test
    public void shouldBuildJavaReflection() {
        EtlFieldAdapter adapter = createTestAdapter(
                EtlJavaReflectionFactory.fieldReader(TestInClass.class),
                EtlJavaReflectionFactory.fieldWriter(TestOutClass.class)
        );

        TestInClass source = createTestInClass();
        TestOutClass target = createTestOutClass();

        adapter.adapt(source, target);

        assertEquals("str1", target.getField1());
        assertEquals(Integer.valueOf(2), target.getField2());
    }
}
