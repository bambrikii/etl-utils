package org.bambrikii.etl.model.transformer.adapters.yaml;

import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoModelWriter;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class EtlYamlModelWriter extends EtlPojoModelWriter {
    public EtlYamlModelWriter() {
        super();
    }

    public EtlYamlModelWriter(Object obj) {
        super(obj);
    }

    public void writeFile(String fileName) throws IOException {
        Object data = this.getTarget();
        DumperOptions options = new DumperOptions();
        options.setCanonical(false);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        try (FileOutputStream stream = new FileOutputStream(fileName);
             OutputStreamWriter writer = new OutputStreamWriter(stream);
             BufferedWriter buffered = new BufferedWriter(writer);
        ) {
            yaml.dump(data, buffered);
        }
    }
}
