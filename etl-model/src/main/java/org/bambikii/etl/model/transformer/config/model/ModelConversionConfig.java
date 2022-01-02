package org.bambikii.etl.model.transformer.config.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelConversionConfig {
    @XmlAttribute
    private String name;

    @XmlAttribute(name = "sourceModel")
    private String sourceModel;

    @XmlAttribute(name = "sourceReaderType")
    private String sourceReaderType;

    @XmlAttribute(name = "targetModel")
    private String targetModel;

    @XmlAttribute(name = "targetReaderType")
    private String targetReaderType;

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    private List<FieldCoversionConfig> fields = new ArrayList<>();
}
