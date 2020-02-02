package org.bambrikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlModelAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.INT;
import static org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy.STRING;

public class EtlTreeConversionTest {

    @Test
    public void shouldTransform() {
        EtlTreeFieldReaderStrategy readerStrategy = EtlTreeAdapterFactory.createTreeFieldReader();
        EtlTreeFieldWriterStrategy writerStrategy = EtlTreeAdapterFactory.createTreeFieldWriter();
        EtlModelAdapter adapter = new EtlModelAdapter(
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

        adapter.adapt(
                EtlTreeAdapterFactory.createTreeInputAdapter(data),
                EtlTreeAdapterFactory.createTreeOutputAdapter()
        );
    }
}
