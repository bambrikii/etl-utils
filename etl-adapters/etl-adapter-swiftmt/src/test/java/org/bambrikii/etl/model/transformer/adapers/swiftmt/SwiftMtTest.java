package org.bambrikii.etl.model.transformer.adapers.swiftmt;

import org.apache.commons.io.IOUtils;
import org.bambikii.etl.model.transformer.builders.EtlAdapterConfigBuilder;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeAdapterFactory;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeOutputFactory;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBException;
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
        EtlTreeOutputFactory outputAdapter = EtlTreeAdapterFactory.createTreeOutputAdapter();
        new EtlAdapterConfigBuilder()
                .readerStrategy(EtlSwiftMtAdapterFactory.createSwiftMtFieldReader())
                .writerStrategy(EtlTreeAdapterFactory.createTreeFieldWriter())
                .modelConfig(SwiftMtTest.class.getResourceAsStream("/model-config.xml"))
                .conversionConfig(SwiftMtTest.class.getResourceAsStream("/mapping-config.xml"))
                .buildMap()
                .get("conversion1")
                .adapt(
                        EtlSwiftMtAdapterFactory.createSwiftMtInputAdapter(swiftMtMessage),
                        outputAdapter
                );

        Object result = outputAdapter.getTarget();
        assertThat(result).isNotNull();

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Test
    public void shouldConvertFromObject() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
