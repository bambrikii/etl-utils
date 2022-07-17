package org.bambrikii.etl.model.transformer.schema;

import jakarta.xml.bind.JAXBException;
import org.bambrikii.etl.model.transformer.mapping.EtlMappingXmlMarshaller;
import org.bambrikii.etl.model.transformer.mapping.model.MappingRoot;
import org.bambrikii.etl.model.transformer.schema.model.SchemaRoot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EtlSchemaXmlMarshallerTest {
    @Test
    public void shouldUnmarshallModels() throws JAXBException {
        SchemaRoot config = EtlSchemaXmlMarshaller.unmarshalModelConfig(EtlSchemaXmlMarshallerTest.class.getResourceAsStream("schema.xml"));

        assertNotNull(config);
    }

    @Test
    public void shouldUnmarshallConversion() throws JAXBException {
        MappingRoot config = EtlMappingXmlMarshaller.unmarshalConversionConfig(EtlSchemaXmlMarshallerTest.class.getResourceAsStream("mappings.xml"));

        assertNotNull(config);
    }
}
