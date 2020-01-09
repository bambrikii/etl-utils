package org.bambikii.etl.model.transformer.adapters;

import java.util.ArrayList;
import java.util.List;

public class ModelAdapter<T, R, W> {
    private List<FieldTransformerPair> fieldTransformers = new ArrayList<>();

    public <O> void addFieldTransformer(FieldReaderAdapter<R, O> readerAdapter, FieldWriterAdapter<W, O> writerAdapter) {
        FieldTransformerPair pair = new FieldTransformerPair();
        pair.setFieldReader(readerAdapter);
        pair.setFieldWriter(writerAdapter);
        fieldTransformers.add(pair);
    }

    public void read(T obj) {
        for (FieldTransformerPair pair : fieldTransformers) {
            FieldWriterAdapter writer = pair.getFieldWriter();
            FieldReaderAdapter reader = pair.getFieldReader();
            writer.writeField(obj, reader.readField(obj));
        }
    }
}
