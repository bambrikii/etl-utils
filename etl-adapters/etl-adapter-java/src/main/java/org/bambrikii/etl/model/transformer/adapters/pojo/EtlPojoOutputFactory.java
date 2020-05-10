package org.bambrikii.etl.model.transformer.adapters.pojo;

import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambrikii.etl.model.transformer.adapters.pojo.io.resultsets.PojoStatement;

import java.util.HashMap;
import java.util.Objects;

public class EtlPojoOutputFactory implements EtlModelOutputFactory<PojoStatement> {
    private final Object target;

    public EtlPojoOutputFactory() {
        this(new HashMap<String, Object>());
    }

    public EtlPojoOutputFactory(Object target) {
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

    public Object getTarget() {
        return target;
    }
}
