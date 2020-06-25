package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambikii.etl.model.transformer.adapters.EtlModelInputFactory;
import org.bambikii.etl.model.transformer.adapters.EtlModelOutputFactory;
import org.bambikii.etl.model.transformer.builders.EtlFieldReaderStrategy;
import org.bambikii.etl.model.transformer.builders.EtlFieldWriterStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EtlDbAdapterFactory {
    private EtlDbAdapterFactory() {
    }

    public static EtlFieldReaderStrategy<ResultSet> createDbFieldReader() {
        return new EtlDbFieldReaderAdapter();
    }

    public static EtlFieldWriterStrategy<PreparedStatement> createDbFieldWriter() {
        return new EtlDbFieldWriterAdapter();
    }

    public static EtlModelInputFactory<ResultSet> createDbInputAdapter(Connection cn, String selectQuery) {
        return new EtlDbInputFactory(cn, selectQuery);
    }

    public static EtlModelOutputFactory<PreparedStatement> createDbOutputAdapter(Connection cn, String insertQuery) {
        return new EtlDbOutputFactory(cn, insertQuery);
    }
}
