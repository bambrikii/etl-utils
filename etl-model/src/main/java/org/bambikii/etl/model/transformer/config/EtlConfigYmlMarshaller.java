package org.bambikii.etl.model.transformer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;

import java.io.IOException;
import java.io.InputStream;

public class EtlConfigYmlMarshaller {
    private static final ObjectMapper mapper;

    static {
        ObjectMapper m = new ObjectMapper(new YAMLFactory());
        m.findAndRegisterModules();
        mapper = m;
    }

    public static ModelRootConfig unmarshalModelConfig(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, ModelRootConfig.class);
    }

    public static ConversionRootConfig unmarshalConversionConfig(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, ConversionRootConfig.class);
    }
}
