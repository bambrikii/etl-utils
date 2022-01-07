package org.bambrikii.etl.model.transformer.adapters.yaml;

import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoModelReader;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class EtlYamlModelReader extends EtlPojoModelReader {
    private EtlYamlModelReader(Object data) {
        super(data);
    }

    public static EtlYamlModelReader fromFile(String fileName) throws IOException {
        try (FileInputStream stream = new FileInputStream(fileName)) {
            return getEtlYamlModelReader(stream, new Yaml());
        }
    }

    private static EtlYamlModelReader getEtlYamlModelReader(InputStream stream, Yaml yaml) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(stream);
             BufferedReader buffered = new BufferedReader(reader);
        ) {
            Object data = yaml.load(buffered);
            return new EtlYamlModelReader(data);
        }
    }

    public static EtlYamlModelReader fromStream(InputStream inputStream) throws IOException {
        return getEtlYamlModelReader(inputStream, new Yaml());
    }
}
