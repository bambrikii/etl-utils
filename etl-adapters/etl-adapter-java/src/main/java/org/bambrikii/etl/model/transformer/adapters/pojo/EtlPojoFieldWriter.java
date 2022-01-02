package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlFieldLoadable;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriter;
import org.bambikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoStatement;

import static org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoModelReader.ETL_POJO;

public class EtlPojoFieldWriter extends EtlFieldWriter<PojoStatement> implements EtlNamable {
    @Override
    public String getName() {
        return ETL_POJO;
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
