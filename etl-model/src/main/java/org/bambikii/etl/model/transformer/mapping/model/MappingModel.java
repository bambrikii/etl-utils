package org.bambikii.etl.model.transformer.mapping.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MappingModel {
    @XmlAttribute
    private String name;

    @XmlAttribute(name = "sourceSchema")
    private String sourceSchema;

    @XmlAttribute(name = "sourceModel")
    private String sourceModel;

    @XmlAttribute(name = "sourceReaderType")
    private String sourceReaderType;

    @XmlAttribute(name = "targetSchema")
    private String targetSchema;

    @XmlAttribute(name = "targetModel")
    private String targetModel;

    @XmlAttribute(name = "targetReaderType")
    private String targetReaderType;

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    @JsonProperty("fields")
    private List<MappingField> fields = new ArrayList<>();
}
