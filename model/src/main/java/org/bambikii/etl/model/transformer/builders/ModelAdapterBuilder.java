package org.bambikii.etl.model.transformer.builders;

import org.bambikii.etl.model.transformer.adapters.FieldReaderAdapter;
import org.bambikii.etl.model.transformer.adapters.FieldTransformerPair;
import org.bambikii.etl.model.transformer.adapters.FieldWriterAdapter;
import org.bambikii.etl.model.transformer.adapters.ModelAdapter;

import java.util.ArrayList;

public class ModelAdapterBuilder<T> {
    private final FieldReaderStrategy fieldReaderStrategy;
    private final FieldWriterStrategy fieldWriterStrategy;
    private final ArrayList<FieldTransformerPair> transformers;

    public ModelAdapterBuilder(
            FieldReaderStrategy fieldReaderStrategy,
            FieldWriterStrategy fieldWriterStrategy
    ) {
        this.fieldReaderStrategy = fieldReaderStrategy;
        this.fieldWriterStrategy = fieldWriterStrategy;
        this.transformers = new ArrayList<>();
    }

    public ModelAdapterBuilder<T> field(String sourceField, String targetField, String type) {
        return field(sourceField, type, targetField, type);
    }

    public ModelAdapterBuilder<T> field(
            String sourceField, String sourceType,
            String targetField, String targetType
    ) {
        FieldReaderAdapter fieldReader = fieldReaderStrategy.createOne(sourceField, sourceType);
        FieldWriterAdapter fieldWriter = fieldWriterStrategy.createOne(targetField, targetType);
        FieldTransformerPair pair = new FieldTransformerPair(fieldReader, fieldWriter);
        transformers.add(pair);
        return this;
    }

    public ModelAdapter build() {
        return new ModelAdapter(transformers);
    }
}
