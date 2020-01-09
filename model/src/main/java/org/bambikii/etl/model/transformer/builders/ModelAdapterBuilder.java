package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;
import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelAdapter;

public class ModelAdapterBuilder<T> {
    private final ModelAdapter<T> adapter;
    private final FieldReaderFactory fieldReaderFactory;
    private final FieldWriterFactory fieldWriterFactory;

    public ModelAdapterBuilder(
            FieldReaderFactory fieldReaderFactory,
            FieldWriterFactory fieldWriterFactory
    ) {
        adapter = new ModelAdapter<>();
        this.fieldReaderFactory = fieldReaderFactory;
        this.fieldWriterFactory = fieldWriterFactory;
    }

    public <O> ModelAdapterBuilder<T> field(
            String sourceField, String sourceType,
            String targetField, String targetType
    ) {
        FieldReaderAdapter<T, O> readerAdapter = fieldReaderFactory.createOne(sourceField, sourceType);
        FieldWriterAdapter<T, O> writerAdapter = fieldWriterFactory.createOne(targetField, targetType);
        adapter.addFieldTransformer(
                readerAdapter,
                writerAdapter
        );
        return this;
    }
}
