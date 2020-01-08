/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bambikii.etl.model.transformer.config;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Alexander_Arakelian
 */
@Getter
@Setter
public class ModelConfig {

    private String name;
    private String type;
    private Integer length;
    private Integer scale;
    private Integer precision;
    private Boolean nullable;
}
