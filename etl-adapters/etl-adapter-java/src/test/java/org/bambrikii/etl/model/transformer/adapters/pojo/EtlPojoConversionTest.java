package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambrikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambrikii.etl.model.transformer.builders.TransformBuilder;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoResultSet;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EtlPojoConversionTest {
    @Test
    public void shouldTransform() {
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "field1 value");
        data.put("field2", 1);

        EtlModelReader<PojoResultSet> modelReader = EtlPojoAdapterFactory.createPojoReader(data);
        EtlPojoModelWriter modelWriter = EtlPojoAdapterFactory.createPojoWriter();

        new TransformBuilder()
                .modelReader(modelReader)
                .modelWriter(modelWriter)
                .fieldMapString("field1", "field1_str")
                .fieldMapInt("field2", "field2_int")
                .transform();

        Object target = modelWriter.getTarget();

        assertThat(target).extracting("field1_str").contains("field1 value");
        assertThat(target).extracting("field2_int").contains(Integer.valueOf(1));
    }
}
