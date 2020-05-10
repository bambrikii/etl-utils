package org.bambrikii.etl.model.transformer.adapters.pojo.io.cursors;

import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldDescriptorsContainer {
    public static final FieldDescriptor NOT_AVAILABLE_FIELD_DESCRIPTOR = new FieldDescriptor(null, null, -1, false, null) {
    };

    private static final String ARRAY_SUFFIX = "[]";
    private final Map<String, List<FieldNameElement>> namesArr = new HashMap<>();
    private final Map<String, Map<Integer, String>> distinctNamesByFullNameAndPos = new HashMap<>();
    private final Map<String, FieldDescriptor> byDistinctName = new HashMap<>();

    public FieldDescriptor ensureFieldDescriptor(String fullName, int pos) {
        if (pos < 0) {
            return null;
        }
        String distinctName = ensureDistinctName(fullName, pos);
        if (distinctName == null) {
            return NOT_AVAILABLE_FIELD_DESCRIPTOR;
        }
        if (byDistinctName.containsKey(distinctName)) {
            return byDistinctName.get(distinctName);
        }

        FieldNameElement nameElement = ensureSimpleName(fullName, pos);
        List<FieldNameElement> namesArr = ensureNamesArr(fullName);
        boolean isLeaf = pos == namesArr.size() - 1;
        FieldDescriptor parentDescriptor = ensureFieldDescriptor(fullName, pos - 1);
        FieldDescriptor fieldDescriptor = new FieldDescriptor(
                nameElement, distinctName,
                pos, isLeaf,
                parentDescriptor
        );
        byDistinctName.put(distinctName, fieldDescriptor);
        return fieldDescriptor;
    }

    private FieldNameElement ensureSimpleName(String fullName, int pos) {
        List<FieldNameElement> arr = ensureNamesArr(fullName);
        if (pos > arr.size() - 1) {
            throw new EtlRuntimeException("FullName's [" + fullName + "] position [" + pos + "] overflows.");
        }
        return arr.get(pos);

    }

    private List<FieldNameElement> ensureNamesArr(String fullName) {
        if (namesArr.containsKey(fullName)) {
            return namesArr.get(fullName);
        }
        List<FieldNameElement> namesArr = splitFields(fullName);
        this.namesArr.put(fullName, namesArr);
        return namesArr;
    }

    public static List<FieldNameElement> splitFields(String fullName) {
        List<FieldNameElement> fields = new ArrayList<>();
        int nameFrom = 0;
        int nameTo = -1;
        boolean inType = false;
        int typeFrom = -1;
        int length = fullName.length();
        Class<?> type = null;
        boolean isArray = false;
        for (int i = 0; i < length; i++) {
            char c = fullName.charAt(i);
            switch (c) {
                case '[':
                    nameTo = i;
                    isArray = true;
                    break;
                case '<': // starting type
                    inType = true;
                    typeFrom = i;
                    break;
                case '>': // ending type
                    inType = false;
                    type = tryClass(fullName, typeFrom, i);
                    break;
                case '.': // field split
                    if (!inType) {
                        fields.add(new FieldNameElement(
                                extractSimpleName(fullName, nameFrom, nameTo, i, typeFrom),
                                type,
                                isArray
                        ));
                        nameFrom = i + 1;
                        nameTo = -1;
                        type = null;
                        isArray = false;
                    }
                    break;
                default:
                    break;
            }
        }
        if (nameFrom <= length) {
            fields.add(new FieldNameElement(
                    extractSimpleName(fullName, nameFrom, nameTo, length, typeFrom),
                    type,
                    isArray
            ));
        }
        return fields;
    }

    private static Class<?> tryClass(String fullName, int typeFrom, int i) {
        if (typeFrom + 1 >= i) {
            return null;
        }
        String className = fullName.substring(typeFrom + 1, i);
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new EtlRuntimeException("Failed to find class <" + className + ">!", ex);
        }
    }

    private static String extractSimpleName(String fullName, int nameFrom, int nameTo, int currentPos, int typeFrom) {
        String name;
        if (typeFrom > nameFrom) {
            name = fullName.substring(nameFrom, nameTo != -1 ? nameTo : typeFrom);
        } else {
            name = fullName.substring(nameFrom, nameTo != -1 ? nameTo : currentPos);
        }
        return name;
    }

    private String ensureDistinctName(String fullName, int pos) {
        if (!distinctNamesByFullNameAndPos.containsKey(fullName)) {
            String distinctName = buildDistinctName(fullName, pos);
            if (distinctName == null) {
                return null;
            }
            Map<Integer, String> posMap = new HashMap<>();
            posMap.put(pos, distinctName);
            distinctNamesByFullNameAndPos.put(fullName, posMap);
            return distinctName;
        }
        Map<Integer, String> posMap = distinctNamesByFullNameAndPos.get(fullName);
        if (posMap.containsKey(pos)) {
            return posMap.get(pos);
        }
        String distinctName = buildDistinctName(fullName, pos);
        if (distinctName == null) {
            return null;
        }
        posMap = new HashMap<>();
        posMap.put(pos, distinctName);
        return distinctName;
    }

    private String buildDistinctName(String fullName, int pos) {
        List<FieldNameElement> namesArr = ensureNamesArr(fullName);
        int size = namesArr.size();
        if (size == 0) {
            throw new EtlRuntimeException("Name should have at least one simple name!");
        }
        if (pos >= size) {
            return null;
        }
        StringBuilder distinctNameBuilder = new StringBuilder();
        appendToDistinctName(namesArr, distinctNameBuilder, 0);
        for (int i = 1; i <= pos; i++) {
            distinctNameBuilder.append(".");
            appendToDistinctName(namesArr, distinctNameBuilder, i);
        }
        return distinctNameBuilder.toString();
    }

    private void appendToDistinctName(List<FieldNameElement> namesArr, StringBuilder distinctNameBuilder, int i) {
        FieldNameElement nameElement = namesArr.get(i);
        String name = nameElement.getSimpleName();
        if (name.endsWith(ARRAY_SUFFIX)) {
            distinctNameBuilder.append(name, 0, name.length() - ARRAY_SUFFIX.length());
        } else {
            distinctNameBuilder.append(name);
        }
    }
}
