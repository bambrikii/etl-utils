package org.bambikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.EtlFieldAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlUtils;
import org.bambikii.etl.model.transformer.builders.EtlConverterBuilder;
import org.bambrikii.etl.model.transformer.adapters.java.map.EtlJavaMapFactory;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlConverterBuilderTest {
    @Test
    public void shouldConvert() throws JAXBException {
        EtlConverterBuilder builder = new EtlConverterBuilder();
        Map<String, EtlFieldAdapter> adapters = builder
                .readerStrategy("java-map", EtlJavaMapFactory.fieldReader())
                .writerStrategy("java-map", EtlJavaMapFactory.fieldWriter())
                .modelConfig(EtlConverterBuilderTest.class.getResourceAsStream("/model-config.xml"))
                .converterConfig(EtlConverterBuilderTest.class.getResourceAsStream("/converter-config.xml"))
                .build();

        Map<String, Object> source = new HashMap<>();
        source.put("stringField", "str1");
        source.put("intField", 2);
        source.put("doubleField", 3.4);

        Map<String, Object> target = new HashMap<>();

        EtlUtils.transform(
                adapters.get("conversion1"),
                EtlJavaMapFactory.createJavaInputAdapter(source),
                EtlJavaMapFactory.createJavaOutputAdapter(target)
        );

        assertEquals("str1", target.get("stringField_2"));
        assertEquals(2, target.get("intField_2"));
        assertEquals(3.4, target.get("doubleField_2"));
    }
}
