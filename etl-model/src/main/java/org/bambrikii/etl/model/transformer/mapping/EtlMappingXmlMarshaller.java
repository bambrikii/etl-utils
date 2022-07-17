package org.bambrikii.etl.model.transformer.mapping;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.bambrikii.etl.model.transformer.mapping.model.MappingRoot;

import java.io.InputStream;

public class EtlMappingXmlMarshaller {
    private static final JAXBContext MAPPING_CONTEXT;

    static {
        try {
            MAPPING_CONTEXT = JAXBContext.newInstance(MappingRoot.class);
        } catch (JAXBException ex) {
            throw new RuntimeException("Failed to initialize context", ex);
        }
    }

    private EtlMappingXmlMarshaller() {
    }

    public static MappingRoot unmarshalConversionConfig(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = MAPPING_CONTEXT.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (MappingRoot) obj;
    }
}
