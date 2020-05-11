package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import org.bambikii.etl.model.transformer.cursors.AbstractFieldDescriptorsContainer;

import java.util.HashMap;
import java.util.Map;

import static org.bambikii.etl.model.transformer.cursors.AbstractFieldDescriptor.ARRAY_SUFFIX;
import static org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtNameType.BLOCK;
import static org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtNameType.COMPONENT;
import static org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtNameType.SEQUENCE;
import static org.bambrikii.etl.model.transformer.adapers.swiftmt.io.SwiftMtNameType.TAG;

public class SwiftMtFieldDescriptorContainer extends AbstractFieldDescriptorsContainer {
    private Map<String, String[]> nameArraysByFullName = new HashMap<>();
    private Map<SwiftMtNameElement, SwiftMtFieldDescriptor> byDistinctSwiftName = new HashMap<>();

    public SwiftMtNameElement ensureDistinctName(String fullName) {
        String[] names = findCachedNamesArray(fullName);

        String sequenceName = names[0];
        boolean isSequenceList = sequenceName.endsWith(ARRAY_SUFFIX);
        if (isSequenceList) {
            sequenceName = trimArrayPostfix(sequenceName);
        }

        String blockName = names[1];
        boolean isBlockList = blockName.endsWith(ARRAY_SUFFIX);
        if (isBlockList) {
            blockName = trimArrayPostfix(blockName);
        }

        String tagName = names[2];
        boolean isTagList = tagName.endsWith(ARRAY_SUFFIX);
        if (isTagList) {
            tagName = trimArrayPostfix(tagName);
        }
        String qualifier = names[3];

        Integer component = Integer.parseInt(names[4]);

        return new SwiftMtNameElement(
                sequenceName, isSequenceList,
                blockName, isBlockList,
                tagName, isTagList,
                qualifier, component, COMPONENT
        );
    }

    private String trimArrayPostfix(String sequenceName) {
        return sequenceName.substring(0, sequenceName.length() - ARRAY_SUFFIX.length());
    }

    private String[] findCachedNamesArray(String fullName) {
        if (nameArraysByFullName.containsKey(fullName)) {
            return nameArraysByFullName.get(fullName);
        }
        String[] names = fullName.split(":");
        nameArraysByFullName.put(fullName, names);
        return names;
    }

    public SwiftMtFieldDescriptor ensureFieldDescriptor(SwiftMtNameElement distinctName) {
        if (byDistinctSwiftName.containsKey(distinctName)) {
            return byDistinctSwiftName.get(distinctName);
        }

        SwiftMtFieldDescriptor parentDescriptor = ensureParentFieldDescriptor(distinctName);
        SwiftMtFieldDescriptor fieldDescriptor = new SwiftMtFieldDescriptor(distinctName, false, parentDescriptor);

        byDistinctSwiftName.put(distinctName, fieldDescriptor);
        return fieldDescriptor;
    }

    private SwiftMtFieldDescriptor ensureParentFieldDescriptor(SwiftMtNameElement distinctName) {
        SwiftMtNameElement parentDistinctName = createParentName(distinctName);
        if (parentDistinctName == null) {
            return null;
        }
        return ensureFieldDescriptor(parentDistinctName);
    }

    protected SwiftMtNameElement createParentName(SwiftMtNameElement name) {
        SwiftMtNameType type = name.getType();
        if (type == null) {
            return null;
        }
        switch (type) {
            case SEQUENCE:
                return new SwiftMtNameElement(
                        null, false,
                        null, false,
                        null, false, null,
                        null,
                        null
                );
            case BLOCK:
                return new SwiftMtNameElement(
                        name.getSequence(), name.isSequenceList(),
                        null, false,
                        null, false, null,
                        null,
                        SEQUENCE
                );
            case TAG:
                return new SwiftMtNameElement(
                        name.getSequence(), name.isSequenceList(),
                        name.getBlock(), name.isBlockList(),
                        null, false, null,
                        null,
                        BLOCK
                );
            case QUALIFIER:
                return new SwiftMtNameElement(
                        name.getSequence(), name.isSequenceList(),
                        name.getBlock(), name.isBlockList(),
                        name.getTag(), name.isTagList(), null,
                        null,
                        BLOCK
                );
            case COMPONENT:
                return new SwiftMtNameElement(
                        name.getSequence(), name.isSequenceList(),
                        name.getBlock(), name.isBlockList(),
                        name.getTag(), name.isTagList(), name.getQualifier(),
                        null,
                        TAG
                );
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
