package org.bambrikii.etl.model.transformer.adapters.yaml;

import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoFieldReader;
import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoFieldWriter;

import java.io.IOException;
import java.io.InputStream;

public class EtlYamlAdapterFactory {
    public static final String ETL_YAML_NAME = "yaml";

    private EtlYamlAdapterFactory() {
    }

    public static EtlPojoFieldReader createYamlFieldReader() {
        return new EtlPojoFieldReader();
    }

    public static EtlPojoFieldWriter createYamlFieldWriter() {
        return new EtlPojoFieldWriter();
    }

    public static EtlYamlModelWriter createYamlWriter() {
        return new EtlYamlModelWriter();
    }

    public static EtlYamlModelWriter createYamlWriter(Object obj) {
        return new EtlYamlModelWriter(obj);
    }

    public static EtlYamlModelReader createYamlReader(String fileName) throws IOException {
        return EtlYamlModelReader.fromFile(fileName);
    }
    public static EtlYamlModelReader createYamlReader(InputStream inputStream) throws IOException {
        return EtlYamlModelReader.fromStream(inputStream);
    }
}
