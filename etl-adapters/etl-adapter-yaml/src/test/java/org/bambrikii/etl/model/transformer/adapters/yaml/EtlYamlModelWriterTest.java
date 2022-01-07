package org.bambrikii.etl.model.transformer.adapters.yaml;

import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoAdapterFactory;
import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoFieldReader;
import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoFieldWriter;
import org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoModelWriter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.INT;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.MAP;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.STRING;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EtlYamlModelWriterTest {
    private EtlPojoFieldReader fieldReader;
    private EtlPojoFieldWriter fieldWriter;

    @BeforeEach
    public void before() {
        fieldReader = EtlPojoAdapterFactory.createPojoFieldReader();
        fieldWriter = EtlYamlAdapterFactory.createYamlFieldWriter();
    }


    @Test
    public void shouldWriteToFile() throws IOException {
        EtlModelAdapter adapter = new EtlModelAdapter(Arrays.asList(
                new EtlFieldConversionPair(fieldReader.createOne("field1", STRING), fieldWriter.createOne("field0.field1", STRING)),
                new EtlFieldConversionPair(fieldReader.createOne("field2", INT), fieldWriter.createOne("field0.field2", INT)),
                new EtlFieldConversionPair(fieldReader.createOne("field3", STRING), fieldWriter.createOne("field0.field3", STRING)),
                new EtlFieldConversionPair(fieldReader.createOne("field4.field5[].", STRING), fieldWriter.createOne("field0.field4[].", STRING)),
                new EtlFieldConversionPair(fieldReader.createOne("field6", MAP), fieldWriter.createOne("field0.field6", MAP))
        ));

        Map source = Map.of(
                "field1", "str1",
                "field2", 2,
                "field3", Calendar.getInstance().getTime().toString(),
                "field4", Map.of("field5", List.of("111", "112", "113")),
                "field6", Map.of("field7", "7", "field8", "8", "field9", "9")
        );

        Map<String, Object> target = new HashMap<>();

        EtlYamlModelWriter yamlWriter = EtlYamlAdapterFactory.createYamlWriter(target);
        adapter.adapt(EtlPojoAdapterFactory.createPojoReader(source), yamlWriter);

        String fileName = "build/test.yaml";

        yamlWriter.writeFile(fileName);

        assertTrue(Files.exists(Paths.get(fileName)));
    }

    @Test
    public void shouldReadFromFile() throws IOException {
        EtlModelAdapter adapter = new EtlModelAdapter(Arrays.asList(
                new EtlFieldConversionPair(fieldReader.createOne("field0.field1", STRING), fieldWriter.createOne("field1", STRING)),
                new EtlFieldConversionPair(fieldReader.createOne("field0.field2", INT), fieldWriter.createOne("field2", INT)),
                new EtlFieldConversionPair(fieldReader.createOne("field0.field3", STRING), fieldWriter.createOne("field3", STRING)),
                new EtlFieldConversionPair(fieldReader.createOne("field0.field4[].", STRING), fieldWriter.createOne("field4.field5[].", STRING)),
                new EtlFieldConversionPair(fieldReader.createOne("field0.field6", MAP), fieldWriter.createOne("field6", MAP))
        ));

        Map<String, Object> target = new HashMap<>();
        EtlPojoModelWriter pojoWriter = EtlPojoAdapterFactory.createPojoWriter(target);
        adapter.adapt(EtlYamlAdapterFactory.createYamlReader(EtlYamlModelWriterTest.class.getResourceAsStream("test.yaml")), pojoWriter);

        Map<String, Object> result = (Map<String, Object>) pojoWriter.getTarget();

        assertThat(result.get("field1")).isEqualTo("str1");
        assertThat(result.get("field2")).isEqualTo(2);
        assertThat(result.get("field3")).isNotNull();
        assertThat(((Map) result.get("field4")).get("field5")).isInstanceOf(List.class);
        assertThat(result.get("field6")).isInstanceOf(Map.class);
    }
}
