package org.bambrikii.etl.model.transformer.adapters.pojo;

import jakarta.xml.bind.JAXBException;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.builders.EtlAdapterConfigBuilder;
import org.bambikii.etl.model.transformer.config.EtlConfigXmlMarshaller;
import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EtlAdapterConfigBuilderTest {
    @Test
    public void shouldConvert() throws JAXBException {
        ModelRootConfig modelRoot = EtlConfigXmlMarshaller.unmarshalModelConfig(EtlAdapterConfigBuilderTest.class.getResourceAsStream("/model-config.xml"));
        ConversionRootConfig conversionRoot = EtlConfigXmlMarshaller.unmarshalConversionConfig(EtlAdapterConfigBuilderTest.class.getResourceAsStream("/converter-config.xml"));

        Map<String, EtlModelAdapter> adapters = new EtlAdapterConfigBuilder()
                .readerStrategy(EtlPojoAdapterFactory.createPojoFieldReader())
                .writerStrategy(EtlPojoAdapterFactory.createPojoFieldWriter())
                .modelConfig(modelRoot)
                .conversionConfig(conversionRoot)
                .buildMap();

        EtlModelAdapter modelAdapter = adapters.get("conversion1");

        Map<String, Object> source = new HashMap<>();
        source.put("stringField", "str1");
        source.put("intField", 2);
        source.put("doubleField", 3.4);

        EtlPojoModelWriter outputAdapter = EtlPojoAdapterFactory.createPojoWriter();
        modelAdapter.adapt(EtlPojoAdapterFactory.createPojoReader(source), outputAdapter);

        Map<String, Object> target = (Map<String, Object>) outputAdapter.getTarget();

        assertEquals("str1", target.get("stringField_2"));
        assertEquals(2, target.get("intField_2"));
        assertEquals(3.4, target.get("doubleField_2"));
    }
}
