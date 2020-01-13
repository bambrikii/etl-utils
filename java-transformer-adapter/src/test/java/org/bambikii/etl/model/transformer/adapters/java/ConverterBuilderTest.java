package org.bambikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.ModelAdapter;
import org.bambikii.etl.model.transformer.builders.ConverterBuilder;
import org.bambrikii.etl.model.transformer.adapters.java.JavaMapFactory;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterBuilderTest {
    @Test
    public void shouldConvert() throws JAXBException {
        ConverterBuilder builder = new ConverterBuilder();
        Map<String, ModelAdapter> adapter = builder
                .readerStrategy("java-map", JavaMapFactory.fieldReader())
                .writerStrategy("java-map", JavaMapFactory.fieldWriter())
                .modelConfig(ConverterBuilderTest.class.getResourceAsStream("/model-config.xml"))
                .converterConfig(ConverterBuilderTest.class.getResourceAsStream("/converter-config.xml"))
                .build();

        Map<String, Object> source = new HashMap<>();
        source.put("stringField", "str1");
        source.put("intField", 2);
        source.put("doubleField", 3.4);

        Map<String, Object> target = new HashMap<>();

        adapter.get("conversion1").adapt(source, target);

        assertEquals("str1", target.get("stringField_2"));
        assertEquals(2, target.get("intField_2"));
        assertEquals(3.4, target.get("doubleField_2"));
    }
}
