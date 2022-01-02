package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.INT;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReader.STRING;

public class EtlAdapterTestUtils {
    public static EtlModelAdapter createTestAdapter(
            EtlFieldReader fieldReader,
            EtlFieldWriter fieldWriter
    ) {
        return new EtlModelAdapter(Arrays.asList(
                new EtlFieldConversionPair(fieldReader.createOne("field1", STRING), fieldWriter.createOne("field1", STRING)),
                new EtlFieldConversionPair(fieldReader.createOne("field2", INT), fieldWriter.createOne("field2", INT)))
        );
    }

    public static TestOutClass createTestOutClass() {
        return new TestOutClass();
    }

    public static TestInClass createTestInClass() {
        TestInClass source = new TestInClass();
        source.setField1("str1");
        source.setField2(2);
        return source;
    }

    public static Map<String, Object> createTestOutMap() {
        return new HashMap<>();
    }

    public static Map<String, Object> createTestInMap() {
        Map<String, Object> source = createTestOutMap();
        source.put("field1", "str1");
        source.put("field2", 2);
        return source;
    }
}
