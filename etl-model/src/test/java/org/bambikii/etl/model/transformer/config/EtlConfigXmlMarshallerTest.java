package org.bambikii.etl.model.transformer.config;

import jakarta.xml.bind.JAXBException;
import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EtlConfigXmlMarshallerTest {
    @Test
    public void shouldUnmarshallModels() throws JAXBException {
        ModelRootConfig config = EtlConfigXmlMarshaller.unmarshalModelConfig(EtlConfigXmlMarshallerTest.class.getResourceAsStream("model-config.xml"));

        assertNotNull(config);
    }

    @Test
    public void shouldUnmarshallConversion() throws JAXBException {
        ConversionRootConfig config = EtlConfigXmlMarshaller.unmarshalConversionConfig(EtlConfigXmlMarshallerTest.class.getResourceAsStream("mapping-config.xml"));

        assertNotNull(config);
    }
}
