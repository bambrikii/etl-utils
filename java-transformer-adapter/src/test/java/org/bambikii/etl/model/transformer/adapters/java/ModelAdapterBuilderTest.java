package org.bambikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.ModelAdapter;
import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;
import org.bambikii.etl.model.transformer.builders.ModelAdapterBuilder;
import org.bambrikii.etl.model.transformer.adapters.java.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.bambikii.etl.model.transformer.builders.FieldReaderStrategy.INT;
import static org.bambikii.etl.model.transformer.builders.FieldReaderStrategy.STRING;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelAdapterBuilderTest {
    @Test
    public void shouldBuildJavaMap() {
        FieldReaderStrategy fieldReaderStrategy = JavaMapFactory.fieldReader();
        FieldWriterStrategy fieldWriterStrategy = JavaMapFactory.fieldWriter();
        ModelAdapterBuilder builder = new ModelAdapterBuilder(fieldReaderStrategy, fieldWriterStrategy);
        ModelAdapter adapter = builder
                .field("field1", STRING, "field1", STRING)
                .field("field2", INT, "field2", INT)
                .build();

        Map<String, Object> source = new HashMap<>();
        source.put("field1", "str1");
        source.put("field2", 2);

        Map<String, Object> target = new HashMap<>();

        adapter.adapt(source, target);

        assertEquals("str1", target.get("field1"));
        assertEquals(2, target.get("field2"));
    }

    @Test
    public void shouldBuildJavaReflection() {
        FieldReaderStrategy fieldReaderStrategy = JavaReflectionFactory.fieldReader(TestInClass.class);
        FieldWriterStrategy fieldWriterStrategy = JavaReflectionFactory.fieldWriter(TestOutClass.class);

        ModelAdapterBuilder builder = new ModelAdapterBuilder(fieldReaderStrategy, fieldWriterStrategy);
        ModelAdapter adapter = builder
                .field("field1", STRING, "field1", STRING)
                .field("field2", INT, "field2", INT)
                .build();

        TestInClass source = new TestInClass();
        source.setField1("str1");
        source.setField2(2);

        TestOutClass target = new TestOutClass();

        adapter.adapt(source, target);

        assertEquals("str1", target.getField1());
        assertEquals(Integer.valueOf(2), target.getField2());
    }
}
