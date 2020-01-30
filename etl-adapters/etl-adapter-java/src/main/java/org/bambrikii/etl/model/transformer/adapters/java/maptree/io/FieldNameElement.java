package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

public class FieldNameElement {
    private String name;
    private String type;

    public FieldNameElement(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
