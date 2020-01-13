package org.bambikii.etl.model.transformer.config.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelFieldConfig {
    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "type")
    private String type;

    @XmlAttribute(name = "length")
    private Integer length;

    @XmlAttribute(name = "scale")
    private Integer scale;

    @XmlAttribute(name = "precision")
    private Integer precision;

    @XmlAttribute(name = "nullable")
    private Boolean nullable;
}
