package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoStatement;

public class EtlPojoFieldWriterStrategy extends EtlFieldWriterStrategy<PojoStatement> implements EtlNamable {
    @Override
    public String getName() {
        return EtlPojoFieldReaderStrategy.ETL_POJO;
    }

    @Override
    protected EtlFieldLoadable<PojoStatement, String> getStringWriter(String name) {
        return (obj, value) -> obj.setField(name, (Object) value, String.class);
    }

    @Override
    protected EtlFieldLoadable<PojoStatement, Integer> getIntWriter(String name) {
        return (obj, value) -> obj.setField(name, value, Integer.class);
    }

    @Override
    protected EtlFieldLoadable<PojoStatement, Double> getDoubleWriter(String name) {
        return (obj, value) -> obj.setField(name, value, Double.class);
    }
}
