package org.bambikii.etl.model.transformer.config.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
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
