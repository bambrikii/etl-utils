package org.bambikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlFieldAdapter;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.INT;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.STRING;

public class EtlAdapterTestUtils {
    public static EtlFieldAdapter createTestAdapter(
            EtlFieldReaderStrategy etlFieldReaderStrategy,
            EtlFieldWriterStrategy etlFieldWriterStrategy
    ) {
        return new EtlFieldAdapter(Arrays.asList(
                new EtlFieldConversionPair(
                        etlFieldReaderStrategy.createOne("field1", STRING),
                        etlFieldWriterStrategy.createOne("field1", STRING)
                ),
                new EtlFieldConversionPair(
                        etlFieldReaderStrategy.createOne("field2", INT),
                        etlFieldWriterStrategy.createOne("field2", INT)
                )
        ));
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
