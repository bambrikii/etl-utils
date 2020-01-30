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
    private final Map<String, List<FieldNameElement>> namesArr = new HashMap<String, List<FieldNameElement>>();
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
        FieldNameElement nameElement = ensureSimpleName(fullName, pos);
        String simpleName = nameElement.getName();
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
        List<FieldNameElement> namesArr = ensureNamesArr(fullName);
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
//        List<String> namesArr = Arrays.asList(fullName.split("\\.", -1));
        List<FieldNameElement> namesArr = splitFields(fullName);
        this.namesArr.put(fullName, namesArr);
        return namesArr;
    }

    public static List<FieldNameElement> splitFields(String fullName) {
        List<FieldNameElement> fields = new ArrayList<>();
        int from = 0;
        boolean inType = false;
        int typeFrom = 0;
        int length = fullName.length();
        String name = null;
        String type = null;
        for (int i = 0; i < length; i++) {
            char c = fullName.charAt(i);
            switch (c) {
                case '<': // starting type
                    inType = true;
                    typeFrom = i;
                    break;
                case '>': // ending type
                    inType = false;
                    type = fullName.substring(typeFrom + 1, i);
                    break;
                case '.': // field split
                    if (!inType) {
                        name = extractSimpleName(fullName, from, typeFrom, i);
                        fields.add(new FieldNameElement(name, type));
                        from = i + 1;
                        name = null;
                        type = null;
                    }
                    break;
                default:
                    break;
            }
        }
        if (from <= length) {
            name = extractSimpleName(fullName, from, typeFrom, length);
            fields.add(new FieldNameElement(name, type));
        }
        return fields;
    }

    private static String extractSimpleName(String fullName, int from, int typeFrom, int i) {
        String name;
        name = typeFrom > from
                ? fullName.substring(from, typeFrom)
                : fullName.substring(from, i);
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
        String name = nameElement.getName();
        if (name.endsWith(ARRAY_SUFFIX)) {
            distinctNameBuilder.append(name, 0, name.length() - ARRAY_SUFFIX.length());
        } else {
            distinctNameBuilder.append(name);
        }
    }
}
