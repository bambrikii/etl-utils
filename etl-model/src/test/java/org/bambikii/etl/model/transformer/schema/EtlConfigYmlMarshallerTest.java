package org.bambikii.etl.model.transformer.schema;

import org.bambikii.etl.model.transformer.mapping.EtlMappingYmlMarshaller;
import org.bambikii.etl.model.transformer.mapping.model.MappingRoot;
import org.bambikii.etl.model.transformer.schema.model.SchemaRoot;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EtlConfigYmlMarshallerTest {
    @Test
    public void shouldUnmarshallModels() throws IOException {
        SchemaRoot config = EtlSchemaYmlMarshaller.unmarshal(EtlConfigYmlMarshallerTest.class.getResourceAsStream("schema.yml"));

        assertNotNull(config);
    }

    @Test
    public void shouldUnmarshallConversion() throws IOException {
        MappingRoot config = EtlMappingYmlMarshaller.unmarshal(EtlConfigYmlMarshallerTest.class.getResourceAsStream("mappings.yml"));

        assertNotNull(config);
    }
}
