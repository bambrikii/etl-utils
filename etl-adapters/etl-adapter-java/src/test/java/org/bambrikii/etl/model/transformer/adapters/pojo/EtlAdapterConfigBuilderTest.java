package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.builders.EtlAdapterConfigBuilder;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlAdapterConfigBuilderTest {
    @Test
    public void shouldConvert() throws JAXBException {
        Map<String, EtlModelAdapter> adapters = new EtlAdapterConfigBuilder()
                .readerStrategy(EtlPojoAdapterFactory.createPojoFieldReader())
                .writerStrategy(EtlPojoAdapterFactory.createPojoFieldWriter())
                .modelConfig(EtlAdapterConfigBuilderTest.class.getResourceAsStream("/model-config.xml"))
                .conversionConfig(EtlAdapterConfigBuilderTest.class.getResourceAsStream("/converter-config.xml"))
                .buildMap();

        EtlModelAdapter modelAdapter = adapters.get("conversion1");

        Map<String, Object> source = new HashMap<>();
        source.put("stringField", "str1");
        source.put("intField", 2);
        source.put("doubleField", 3.4);

        EtlPojoOutputFactory outputAdapter = EtlPojoAdapterFactory.createPojoOutputAdapter();
        modelAdapter.adapt(EtlPojoAdapterFactory.createPojoInputAdapter(source), outputAdapter);

        Map<String, Object> target = (Map<String, Object>) outputAdapter.getTarget();

        assertEquals("str1", target.get("stringField_2"));
        assertEquals(2, target.get("intField_2"));
        assertEquals(3.4, target.get("doubleField_2"));
    }
}
