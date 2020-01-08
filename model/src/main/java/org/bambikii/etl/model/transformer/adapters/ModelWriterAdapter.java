/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bambikii.etl.model.transformer.adapters;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander_Arakelian
 */
public class ModelWriterAdapter<T> {

    private List<FieldWriterAdapter<T>> fieldWriters = new ArrayList<>();

}
