package org.bambikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlFieldAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlUtils;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeFactory;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeFieldReaderStrategy;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeFieldWriterStrategy;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.INT;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.STRING;

public class EtlTreeConversionTest {

    @Test
    public void shouldTransform() {
        EtlTreeFieldReaderStrategy readerStrategy = EtlTreeFactory.jsonFieldReaderStrategy();
        EtlTreeFieldWriterStrategy writerStrategy = EtlTreeFactory.jsonFieldWriterStrategy();
        EtlFieldAdapter adapter = new EtlFieldAdapter(
                new EtlFieldConversionPair(
                        readerStrategy.createOne("field1", STRING),
                        writerStrategy.createOne("field1_2", STRING)
                ),
                new EtlFieldConversionPair(
                        readerStrategy.createOne("field2int", INT),
                        writerStrategy.createOne("field2int_2", INT)
                )
        );
        Map<String, Object> data = new HashMap<>();
        data.put("field1", "field1 value");
        data.put("field2", 1);

        EtlUtils.transform(
                adapter,
                EtlTreeFactory.jsonInput(data),
                EtlTreeFactory.jsonOutput()
        );
    }
}
