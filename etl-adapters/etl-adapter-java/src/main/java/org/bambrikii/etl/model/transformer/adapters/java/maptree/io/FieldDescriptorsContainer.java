package org.bambrikii.etl.model.transformer.adapters.java.maptree.io;

import org.bambikii.etl.model.transformer.adapters.EtlRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldDescriptorsContainer {
    public static final FieldDescriptor NOT_AVAILABLE_FIELD_DESCRIPTOR = new FieldDescriptor(null, null, -1, false, false, null) {
    };
    public static final String ARRAY_SUFFIX = "[]";
    private final Map<String, List<String>> namesArr = new HashMap<>();
    private final Map<String, Map<Integer, String>> distinctNamesByFullNameAndPos = new HashMap<>();
    private final Map<String, FieldDescriptor> byDistinctName = new HashMap<>();
    private final Pattern TYPE_PATTERN = Pattern.compile("^(.*)<(.*)>$");

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

        boolean isArray = false;
        String simpleName = ensureSimpleName(fullName, pos);
        Class<?> cls = null;
        if (simpleName.endsWith(ARRAY_SUFFIX)) {
            simpleName = simpleName.substring(0, simpleName.length() - ARRAY_SUFFIX.length());
            isArray = true;
        } else {
            Matcher matcher = TYPE_PATTERN.matcher(simpleName);
            if (matcher.matches()) {
                simpleName = matcher.group(1);
                try {
                    cls = Class.forName(matcher.group(2));
                    if (cls.isAssignableFrom(List.class)) {
                        isArray = true;
                    }
                } catch (ClassNotFoundException ex) {
                    throw new EtlRuntimeException(ex.getMessage(), ex);
                }
            }
        }
        List<String> namesArr = ensureNamesArr(fullName);
        boolean isLeaf = pos == namesArr.size() - 1;
        FieldDescriptor parentDescriptor = ensureFieldDescriptor(fullName, pos - 1);
        FieldDescriptor fieldDescriptor = new FieldDescriptor(
                simpleName,
                distinctName,
                pos, isArray, cls,
                isLeaf,
                parentDescriptor
        );
        byDistinctName.put(distinctName, fieldDescriptor);
        return fieldDescriptor;
    }

    private String ensureSimpleName(String fullName, int pos) {
        List<String> arr = ensureNamesArr(fullName);
        if (pos > arr.size() - 1) {
            throw new EtlRuntimeException("FullName's [" + fullName + "] position [" + pos + "] overflows.");
        }
        return arr.get(pos);

    }

    private List<String> ensureNamesArr(String fullName) {
        if (namesArr.containsKey(fullName)) {
            return namesArr.get(fullName);
        }
//        List<String> namesArr = Arrays.asList(fullName.split("\\.", -1));
        List<String> namesArr = splitFields(fullName);
        this.namesArr.put(fullName, namesArr);
        return namesArr;
    }

    public static List<String> splitFields(String fullName) {
        List<String> fields = new ArrayList<>();
        int from = 0;
        boolean inType = false;
        int length = fullName.length();
        for (int i = 0; i < length; i++) {
            char c = fullName.charAt(i);
            switch (c) {
                case '<': // starting type
                    inType = true;
                    break;
                case '>': // ending type
                    inType = false;
                    break;
                case '.': // field split
                    if (!inType) {
                        fields.add(fullName.substring(from, i));
                        from = i + 1;
                    }
                    break;
                default:
                    break;
            }
        }
        if (from <= length) {
            fields.add(fullName.substring(from));
        }
        return fields;
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
        List<String> namesArr = ensureNamesArr(fullName);
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

    private void appendToDistinctName(List<String> namesArr, StringBuilder distinctNameBuilder, int i) {
        String name = namesArr.get(i);
        if (name.endsWith(ARRAY_SUFFIX)) {
            distinctNameBuilder.append(name, 0, name.length() - ARRAY_SUFFIX.length());
        } else {
            distinctNameBuilder.append(name);
        }
    }
}
