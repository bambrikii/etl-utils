package org.bambikii.etl.model.transformer.config.model;

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
@XmlRootElement(name = "model-root")
public class ModelRootConfig {
    @XmlElementWrapper(name = "models")
    @XmlElement(name = "model")
    private List<ModelConfig> modelConfigs = new ArrayList<>();
}
