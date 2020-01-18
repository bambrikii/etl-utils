package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.builders.FieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.FieldWriterStrategy;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbAdapterFactory {
    private DbAdapterFactory() {
    }


    public static FieldReaderStrategy<ResultSet> fieldReader() {
        return new DbFieldReaderAdapter();
    }

    public static FieldWriterStrategy<PreparedStatement> fieldWriter() {
        return new DbFieldWriterAdapter();
    }
}
