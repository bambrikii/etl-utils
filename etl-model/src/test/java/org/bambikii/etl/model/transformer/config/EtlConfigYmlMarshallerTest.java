package org.bambikii.etl.model.transformer.config;

import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EtlConfigYmlMarshallerTest {
    @Test
    public void shouldUnmarshallModels() throws IOException {
        ModelRootConfig config = EtlConfigYmlMarshaller.unmarshalModelConfig(EtlConfigYmlMarshallerTest.class.getResourceAsStream("model-config.yml"));

        assertNotNull(config);
    }

    @Test
    public void shouldUnmarshallConversion() throws IOException {
        ConversionRootConfig config = EtlConfigYmlMarshaller.unmarshalConversionConfig(EtlConfigYmlMarshallerTest.class.getResourceAsStream("mapping-config.yml"));

        assertNotNull(config);
    }
}
