package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import jakarta.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.bambrikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambrikii.etl.model.transformer.builders.TransformBuilder;
import org.bambrikii.etl.model.transformer.mapping.EtlMappingXmlMarshaller;
import org.bambrikii.etl.model.transformer.schema.EtlSchemaXmlMarshaller;
import org.bambrikii.etl.model.transformer.mapping.model.MappingRoot;
import org.bambrikii.etl.model.transformer.schema.model.SchemaRoot;
import org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtResultSet;
import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoAdapterFactory;
import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoModelWriter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://coding.pstodulka.com/2015/01/10/anatomy-of-a-swift-message/
 */
public class SwiftMtTest {
    @Test
    public void shouldMapToObject() throws IOException, JAXBException {
        String swiftMtMessage = IOUtils.resourceToString("/MT564.txt", UTF_8);

        EtlModelReader<SwiftMtResultSet> swiftMtInputAdapter = EtlSwiftMtAdapterFactory.createSwiftMtReader(swiftMtMessage);
        EtlPojoModelWriter pojoOutputFactory = EtlPojoAdapterFactory.createPojoWriter();

        SchemaRoot models = EtlSchemaXmlMarshaller.unmarshalModelConfig(SwiftMtTest.class.getResourceAsStream("/schema.xml"));
        MappingRoot mappings = EtlMappingXmlMarshaller.unmarshalConversionConfig(SwiftMtTest.class.getResourceAsStream("/mapping.xml"));

        TransformBuilder
                .of(swiftMtInputAdapter, pojoOutputFactory, models, mappings)
                .mapping("conversion1")
                .transform();

        Object result = pojoOutputFactory.getTarget();
        assertThat(result).isNotNull();
        assertThat(result).extracting("function").contains("NEWM");
        assertThat(result).extracting("linkedMessages").contains("001");
        assertThat(result).extracting("seme").contains("9876543210987654");
    }

    @Test
    @Disabled
    public void shouldMapFromObject() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
