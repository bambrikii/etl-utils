package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

public class FieldDescriptor {
    private final String simpleName;
    private final String distinctName;
    private final boolean array;
    private final int simpleNamePosition;

    public FieldDescriptor(String simpleName, String distinctName,
                           boolean array, int simpleNamePosition
    ) {
        this.simpleName = simpleName;
        this.distinctName = distinctName;
        this.array = array;
        this.simpleNamePosition = simpleNamePosition;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getDistinctName() {
        return distinctName;
    }

    public boolean isArray() {
        return array;
    }

    public int getSimpleNamePosition() {
        return simpleNamePosition;
    }
}
