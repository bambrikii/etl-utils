package org.bambrikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambrikii.etl.model.transformer.adapters.java.map.EtlJavaMapAdapterFactory;
import org.bambrikii.etl.model.transformer.adapters.java.reflection.EtlJavaReflectionAdapterFactory;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlJavaAdapterTest {
    @Test
    public void shouldBuildJavaMap() {
        EtlModelAdapter adapter = EtlAdapterTestUtils.createTestAdapter(
                EtlJavaMapAdapterFactory.createFieldReader(),
                EtlJavaMapAdapterFactory.createFieldWriter()
        );

        Map<String, Object> source = EtlAdapterTestUtils.createTestInMap();
        Map<String, Object> target = EtlAdapterTestUtils.createTestOutMap();

        adapter.adapt(
                EtlJavaReflectionAdapterFactory.createJavaInputAdapter(source),
                EtlJavaReflectionAdapterFactory.createJavaOutputAdapter(target)
        );

        assertEquals("str1", target.get("field1"));
        assertEquals(2, target.get("field2"));
    }

    @Test
    public void shouldBuildJavaReflection() {
        EtlModelAdapter adapter = EtlAdapterTestUtils.createTestAdapter(
                EtlJavaReflectionAdapterFactory.createFieldReader(TestInClass.class),
                EtlJavaReflectionAdapterFactory.createFieldWriter(TestOutClass.class)
        );

        TestInClass source = EtlAdapterTestUtils.createTestInClass();
        TestOutClass target = EtlAdapterTestUtils.createTestOutClass();

        adapter.adapt(
                EtlJavaReflectionAdapterFactory.createJavaInputAdapter(source),
                EtlJavaReflectionAdapterFactory.createJavaOutputAdapter(target)
        );

        assertEquals("str1", target.getField1());
        assertEquals(Integer.valueOf(2), target.getField2());
    }
}
