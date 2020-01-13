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
@XmlRootElement(name = "conversion-root")
public class ConversionRootConfig {
    @XmlElementWrapper(name = "conversions")
    @XmlElement(name = "conversion")
    private List<ModelConversionConfig> modelConversionConfigs = new ArrayList<>();
}
