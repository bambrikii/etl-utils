package org.bambikii.etl.model.transformer.adapters.java;

import org.bambikii.etl.model.transformer.adapters.FieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.ModelFieldAdapter;
import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.bambikii.etl.model.transformer.builders.FieldReaderStrategy.INT;
import static org.bambikii.etl.model.transformer.builders.FieldReaderStrategy.STRING;

public class AdapterTestUtils {
    public static ModelFieldAdapter createTestAdapter(
            FieldReaderStrategy fieldReaderStrategy,
            FieldWriterStrategy fieldWriterStrategy
    ) {
        return new ModelFieldAdapter(Arrays.asList(
                new FieldConversionPair(
                        fieldReaderStrategy.createOne("field1", STRING),
                        fieldWriterStrategy.createOne("field1", STRING)
                ),
                new FieldConversionPair(
                        fieldReaderStrategy.createOne("field2", INT),
                        fieldWriterStrategy.createOne("field2", INT)
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
