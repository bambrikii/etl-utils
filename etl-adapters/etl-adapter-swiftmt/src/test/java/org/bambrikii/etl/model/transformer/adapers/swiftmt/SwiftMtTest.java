package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import jakarta.xml.bind.JAXBException;
import org.apache.commons.io.IOUtils;
import org.bambikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambikii.etl.model.transformer.builders.TransformBuilder;
import org.bambikii.etl.model.transformer.config.EtlConfigXmlMarshaller;
import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;
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
    public void shouldConvertToObject() throws IOException, JAXBException {
        String swiftMtMessage = IOUtils.resourceToString("/MT564.txt", UTF_8);

        EtlModelReader<SwiftMtResultSet> swiftMtInputAdapter = EtlSwiftMtAdapterFactory.createSwiftMtReader(swiftMtMessage);
        EtlPojoModelWriter pojoOutputFactory = EtlPojoAdapterFactory.createPojoWriter();

        ModelRootConfig models = EtlConfigXmlMarshaller.unmarshalModelConfig(SwiftMtTest.class.getResourceAsStream("/model-config.xml"));
        ConversionRootConfig conversions = EtlConfigXmlMarshaller.unmarshalConversionConfig(SwiftMtTest.class.getResourceAsStream("/mapping-config.xml"));

        TransformBuilder
                .of(swiftMtInputAdapter, pojoOutputFactory, models, conversions)
                .fieldsByConversion("conversion1")
                .transform();

        Object result = pojoOutputFactory.getTarget();
        assertThat(result).isNotNull();
        assertThat(result).extracting("function").contains("NEWM");
        assertThat(result).extracting("linkedMessages").contains("001");
        assertThat(result).extracting("seme").contains("9876543210987654");
    }

    @Test
    @Disabled
    public void shouldConvertFromObject() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
