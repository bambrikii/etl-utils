package org.bambrikii.etl.model.transformer.schema.model;

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
public class Schema {
    @XmlAttribute(name = "name")
    private String name;
    @XmlElementWrapper(name = "models")
    @XmlElement(name = "model")
    @JsonProperty("models")
    private List<SchemaModel> models = new ArrayList<>();
}
