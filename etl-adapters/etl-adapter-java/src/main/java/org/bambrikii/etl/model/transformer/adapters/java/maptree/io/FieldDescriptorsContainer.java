package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldDescriptorsContainer {
    public static final String ARRAY_SUFFIX = "[]";
    private Map<String, List<String>> namesArr = new HashMap<>();
    private Map<String, Map<Integer, FieldDescriptor>> fieldDescriptors = new HashMap<>();

    private List<String> ensureNamesSplit(String name) {
        if (namesArr.containsKey(name)) {
            return namesArr.get(name);
        }
        List<String> namesArr = Arrays.asList(name.split("\\."));
        this.namesArr.put(name, namesArr);
        return namesArr;
    }

    public FieldDescriptor getFieldDescriptor(String fullName, int simpleNamePosition) {
        List<String> namesArr = ensureNamesSplit(fullName);
        Map<Integer, FieldDescriptor> descriptorsByPos;
        if (!fieldDescriptors.containsKey(fullName)) {
            descriptorsByPos = new HashMap<>();
            fieldDescriptors.put(fullName, descriptorsByPos);
        } else {
            descriptorsByPos = fieldDescriptors.get(fullName);
        }
        if (!descriptorsByPos.containsKey(simpleNamePosition)) {
            String simpleName = namesArr.get(simpleNamePosition);
            boolean isArray;
            if (simpleName.endsWith(ARRAY_SUFFIX)) {
                simpleName = simpleName.substring(0, simpleName.length() - ARRAY_SUFFIX.length());
                isArray = true;
            } else {
                isArray = false;
            }
            String distinctName = buildDistinctName(namesArr, simpleNamePosition);
            FieldDescriptor fieldDescriptor = new FieldDescriptor(simpleName, distinctName, isArray, simpleNamePosition);
            descriptorsByPos.put(simpleNamePosition, fieldDescriptor);
            return fieldDescriptor;
        }
        return descriptorsByPos.get(simpleNamePosition);
    }

    private String buildDistinctName(List<String> namesArr, int pos) {
        StringBuilder distinctNameBuilder = new StringBuilder();
        for (int i = 0; i <= pos; i++) {
            String name = namesArr.get(i);
            if (name.endsWith(ARRAY_SUFFIX)) {
                distinctNameBuilder.append(name, 0, name.length() - ARRAY_SUFFIX.length());
            }
            if (i > 0) {
                distinctNameBuilder.append(".");
            }
        }
        return distinctNameBuilder.toString();
    }
}
