package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlPojoAdapterTest {
    @Test
    public void shouldBuildJavaMap() {
        EtlModelAdapter adapter = EtlAdapterTestUtils.createTestAdapter(
                EtlPojoAdapterFactory.createPojoFieldReader(),
                EtlPojoAdapterFactory.createPojoFieldWriter()
        );

        Map<String, Object> source = EtlAdapterTestUtils.createTestInMap();

        EtlPojoOutputFactory outputAdapter = EtlPojoAdapterFactory.createPojoOutputAdapter();
        adapter.adapt(
                EtlPojoAdapterFactory.createPojoInputAdapter(source),
                outputAdapter
        );
        Map<String, Object> target = (Map<String, Object>) outputAdapter.getTarget();

        assertEquals("str1", target.get("field1"));
        assertEquals(2, target.get("field2"));
    }

    @Test
    public void shouldBuildJavaReflection() {
        EtlModelAdapter adapter = EtlAdapterTestUtils.createTestAdapter(
                EtlPojoAdapterFactory.createPojoFieldReader(),
                EtlPojoAdapterFactory.createPojoFieldWriter()
        );

        TestInClass source = EtlAdapterTestUtils.createTestInClass();
        TestOutClass target = EtlAdapterTestUtils.createTestOutClass();

        adapter.adapt(
                EtlPojoAdapterFactory.createPojoInputAdapter(source),
                EtlPojoAdapterFactory.createPojoOutputAdapter(target)
        );

        assertEquals("str1", target.getField1());
        assertEquals(Integer.valueOf(2), target.getField2());
    }
}
