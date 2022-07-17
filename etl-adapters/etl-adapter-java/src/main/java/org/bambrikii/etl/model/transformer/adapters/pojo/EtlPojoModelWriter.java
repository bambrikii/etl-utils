package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambrikii.etl.model.transformer.adapters.EtlModelWriter;
import org.bambrikii.etl.model.transformer.builders.EtlFieldWriter;
import org.bambrikii.etl.model.transformer.builders.EtlNamable;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoStatement;

import java.util.HashMap;
import java.util.Objects;

import static org.bambrikii.etl.model.transformer.adapters.pojo.EtlPojoModelReader.ETL_POJO;

public class EtlPojoModelWriter implements EtlModelWriter<PojoStatement>, EtlNamable {
    private final Object target;

    public EtlPojoModelWriter() {
        this(new HashMap<String, Object>());
    }

    public EtlPojoModelWriter(Object target) {
        Objects.requireNonNull(target);
        this.target = target;
    }

    @Override
    public PojoStatement create() {
        return new PojoStatement(target);
    }

    @Override
    public boolean complete(PojoStatement statement) {
        return false;
    }

    @Override
    public EtlFieldWriter<PojoStatement> createFieldWriter() {
        return new EtlPojoFieldWriter();
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public String getName() {
        return ETL_POJO;
    }
}
