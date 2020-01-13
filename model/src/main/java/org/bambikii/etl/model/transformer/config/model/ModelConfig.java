/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bambikii.etl.model.transformer.config.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "model")
public class ModelConfig {
    @XmlAttribute(name = "name")
    private String name;

    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    private List<ModelFieldConfig> modelFields = new ArrayList<>();
}
