package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.EtlFieldAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlUtils;
import org.bambrikii.etl.model.transformer.adapters.java.map.EtlJavaMapFactory;
import org.bambrikii.etl.model.transformer.adapters.java.reflection.EtlJavaReflectionFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlJavaAdapterTest {
    @Test
    public void shouldBuildJavaMap() {
        EtlFieldAdapter adapter = EtlAdapterTestUtils.createTestAdapter(
                EtlJavaMapFactory.fieldReader(),
                EtlJavaMapFactory.fieldWriter()
        );

        Map<String, Object> source = EtlAdapterTestUtils.createTestInMap();
        Map<String, Object> target = EtlAdapterTestUtils.createTestOutMap();

        EtlUtils.transform(
                adapter,
                EtlJavaReflectionFactory.createJavaInputAdapter(source),
                EtlJavaReflectionFactory.createJavaOutputAdapter(target)
        );

        assertEquals("str1", target.get("field1"));
        assertEquals(2, target.get("field2"));
    }

    @Test
    public void shouldBuildJavaReflection() {
        EtlFieldAdapter adapter = EtlAdapterTestUtils.createTestAdapter(
                EtlJavaReflectionFactory.fieldReader(TestInClass.class),
                EtlJavaReflectionFactory.fieldWriter(TestOutClass.class)
        );

        TestInClass source = EtlAdapterTestUtils.createTestInClass();
        TestOutClass target = EtlAdapterTestUtils.createTestOutClass();

        EtlUtils.transform(
                adapter,
                EtlJavaReflectionFactory.createJavaInputAdapter(source),
                EtlJavaReflectionFactory.createJavaOutputAdapter(target)
        );

        assertEquals("str1", target.getField1());
        assertEquals(Integer.valueOf(2), target.getField2());
    }
}
