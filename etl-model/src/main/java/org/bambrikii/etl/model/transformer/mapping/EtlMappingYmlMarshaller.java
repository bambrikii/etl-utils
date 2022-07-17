package org.bambrikii.etl.model.transformer.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bambrikii.etl.model.transformer.mapping.model.MappingRoot;

import java.io.IOException;
import java.io.InputStream;

public class EtlMappingYmlMarshaller {
    private static final ObjectMapper mapper;

    static {
        ObjectMapper m = new ObjectMapper(new YAMLFactory());
        m.findAndRegisterModules();
        mapper = m;
    }

    public static MappingRoot unmarshal(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, MappingRoot.class);
    }
}
