package org.bambikii.etl.model.transformer.config;

import org.bambikii.etl.model.transformer.config.model.ConversionRootConfig;
import org.bambikii.etl.model.transformer.config.model.ModelRootConfig;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class ConfigMarshaller {
    public ModelRootConfig unmarshalModelConfig(InputStream inputStream) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(ModelRootConfig.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (ModelRootConfig) obj;
    }

    public ConversionRootConfig unmarshalConversionConfig(InputStream inputStream) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(ConversionRootConfig.class);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        Object obj = unmarshaller.unmarshal(inputStream);
        return (ConversionRootConfig) obj;
    }
}
