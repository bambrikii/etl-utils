package org.bambikii.etl.model.transformer.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bambikii.etl.model.transformer.schema.model.SchemaRoot;

import java.io.IOException;
import java.io.InputStream;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;

public class EtlSchemaYmlMarshaller {
    private static final ObjectMapper mapper;

    static {
        ObjectMapper m = new ObjectMapper(new YAMLFactory());
        m.findAndRegisterModules();
        m.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        mapper = m;
    }

    public static SchemaRoot unmarshal(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, SchemaRoot.class);
    }
}
