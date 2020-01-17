package org.bambrikii.etl.model.transformer.adapters.h2;

import org.bambikii.etl.model.transformer.adapters.ModelFieldAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelInputAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelOutputAdapter;

import java.sql.Connection;

public class ModelAdapterBuilder {
    public void build() {
        Connection cn = null;
        ModelFieldAdapter modelFieldAdapter = new ModelFieldAdapter();
        ModelOutputAdapter modelOutputAdapter = new DbModelOutputAdapter(cn, "select 1 from dual");
        ModelInputAdapter modelInputAdapter = new DbModelInputAdapter(cn, "select 1 from dual", modelFieldAdapter, modelOutputAdapter);
        modelInputAdapter.adapt();
    }
}
