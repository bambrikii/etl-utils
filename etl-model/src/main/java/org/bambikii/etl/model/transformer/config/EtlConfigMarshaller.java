package org.bambikii.etl.model.transformer.config;

import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class EtlConfigMarshaller {
    private static final JAXBContext MODEL_CONTEXT;
    private static final JAXBContext CONVERTER_CONTEXT;

    static {
        try {
            MODEL_CONTEXT = JAXBContext.newInstance(ModelRootConfig.class);
        } catch (JAXBException ex) {
            throw new RuntimeException("Failed to initialize context", ex);
        }
    }

    static {
        try {
            CONVERTER_CONTEXT = JAXBContext.newInstance(ConversionRootConfig.class);
        } catch (JAXBException ex) {
            throw new RuntimeException("Failed to initialize context", ex);
        }
    }

    private EtlConfigMarshaller() {
    }

    public static ModelRootConfig unmarshalModelConfig(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = MODEL_CONTEXT.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (ModelRootConfig) obj;
    }

    public static ConversionRootConfig unmarshalConversionConfig(InputStream inputStream) throws JAXBException {
        Unmarshaller unmarshaller = CONVERTER_CONTEXT.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (ConversionRootConfig) obj;
    }
}
