package org.bambikii.etl.model.transformer.config.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
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
