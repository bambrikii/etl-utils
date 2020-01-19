package org.bambikii.etl.model.transformer.config;

import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class EtlConfigMarshaller {
    private static final JAXBContext CTX;

    static {
        try {
            CTX = JAXBContext.newInstance(ModelRootConfig.class);
        } catch (JAXBException ex) {
            throw new RuntimeException("Failed to initialize context", ex);
        }
    }

    private EtlConfigMarshaller() {
    }

    public static ModelRootConfig unmarshalModelConfig(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = CTX.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (ModelRootConfig) obj;
    }

    public static ConversionRootConfig unmarshalConversionConfig(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = CTX.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (ConversionRootConfig) obj;
    }
}
