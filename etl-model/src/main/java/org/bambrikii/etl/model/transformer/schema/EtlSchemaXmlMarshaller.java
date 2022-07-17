package org.bambrikii.etl.model.transformer.schema;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.bambrikii.etl.model.transformer.schema.model.SchemaRoot;

import java.io.InputStream;

public class EtlSchemaXmlMarshaller {
    private static final JAXBContext MODEL_CONTEXT;

    static {
        try {
            MODEL_CONTEXT = JAXBContext.newInstance(SchemaRoot.class);
        } catch (JAXBException ex) {
            throw new RuntimeException("Failed to initialize context", ex);
        }
    }

    private EtlSchemaXmlMarshaller() {
    }

    public static SchemaRoot unmarshalModelConfig(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = MODEL_CONTEXT.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (SchemaRoot) obj;
    }
}
