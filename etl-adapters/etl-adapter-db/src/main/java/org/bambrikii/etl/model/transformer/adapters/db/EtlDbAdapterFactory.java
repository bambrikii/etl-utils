package org.bambrikii.etl.model.transformer.adapters.db;

import org.bambrikii.etl.model.transformer.adapters.EtlModelReader;
import org.bambrikii.etl.model.transformer.adapters.EtlModelWriter;
import org.bambrikii.etl.model.transformer.builders.EtlFieldReader;
import org.bambrikii.etl.model.transformer.builders.EtlFieldWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EtlDbAdapterFactory {
    private EtlDbAdapterFactory() {
    }

    public static EtlFieldReader<ResultSet> createDbFieldReader() {
        return new EtlDbFieldReader();
    }

    public static EtlFieldWriter<PreparedStatement> createDbFieldWriter() {
        return new EtlDbFieldWriter();
    }

    public static EtlModelReader<ResultSet> createDbReader(Connection cn, String selectQuery) {
        return new EtlDbModelReader(cn, selectQuery);
    }

    public static EtlModelWriter<PreparedStatement> createDbWriter(Connection cn, String insertQuery) {
        return new EtlDbModelWriter(cn, insertQuery);
    }
}
