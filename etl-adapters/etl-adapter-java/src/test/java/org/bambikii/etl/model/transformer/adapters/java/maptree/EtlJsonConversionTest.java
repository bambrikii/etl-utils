package org.bambikii.etl.model.transformer.adapters.java.maptree;

import org.bambikii.etl.model.transformer.adapters.EtlFieldAdapter;
import org.bambikii.etl.model.transformer.adapters.EtlFieldConversionPair;
import org.bambikii.etl.model.transformer.adapters.EtlUtils;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeFactory;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeFieldReaderStrategy;
import org.bambrikii.etl.model.transformer.adapters.java.maptree.EtlTreeFieldWriterStrategy;
import org.junit.jupiter.api.Test;

public class EtlJsonConversionTest {

    @Test
    public void shouldTransform() {
        EtlTreeFieldReaderStrategy readerStrategy = EtlTreeFactory.jsonFieldReaderStrategy();
        EtlTreeFieldWriterStrategy writerStrategy = EtlTreeFactory.jsonFieldWriterStrategy();
        EtlFieldAdapter adapter = new EtlFieldAdapter(
                new EtlFieldConversionPair(
                        readerStrategy.createOne("field1", "string"),
                        writerStrategy.createOne("field1", "string1")
                )
        );
        EtlUtils.transform(
                adapter,
                EtlTreeFactory.jsonInput(),
                EtlTreeFactory.jsonOutput()

        );
    }
}
