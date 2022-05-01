package org.bambikii.etl.model.transformer.schema.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "schemas")
@JsonRootName("schema")
public class SchemaRoot {
    @XmlElementWrapper(name = "schemas")
    @XmlElement(name = "schema")
    @JsonProperty("schemas")
    private List<Schema> schemas = new ArrayList<>();
}
