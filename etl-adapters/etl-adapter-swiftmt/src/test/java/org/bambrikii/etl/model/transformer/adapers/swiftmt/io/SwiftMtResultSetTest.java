package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

public class SwiftMtResultSetTest {
    @Test
    public void shouldReadComponent() throws IOException {
        String messageAsString = IOUtils.resourceToString("/MT564.txt", UTF_8);
        AbstractMT message = SwiftMessage.parse(messageAsString).toMT();

        SwiftMtResultSet rs = new SwiftMtResultSet(message);
        String val = rs.getObject("A::20C:SEME:2", String.class);

        assertThat(val).isEqualTo("9876543210987654");
    }
}
