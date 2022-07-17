package org.bambrikii.etl.model.transformer.schema.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class SchemaField {
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
