package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.field.Field;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import org.bambikii.etl.model.transformer.cursors.AbstractResultSet;
import org.bambikii.etl.model.transformer.utils.StringUtils;

import java.util.List;

public class SwiftMtResultSet extends AbstractResultSet<
        AbstractMT, SwiftMtFieldDescriptorContainer, SwiftMtCursorsContainer
        > {
    public SwiftMtResultSet(AbstractMT message) {
        super(message, new SwiftMtFieldDescriptorContainer(), new SwiftMtCursorsContainer());
    }

    @Override
    public <T> T getObject(String name, Class<T> valueClass) {
        return (T) readObject0(name, valueClass);
    }

    private Object readObject0(String fullName, Class<?> valueClass) {
        SwiftMtNameElement name = fieldDescriptors.ensureDistinctName(fullName);
        SwiftTagListBlock block = readSequence(name, source);
        SwiftTagListBlock subBlock = readBlock(name, block);
        Field field = readTag(name, subBlock);
        return readComponent(name, valueClass, field);
    }

    private SwiftTagListBlock readSequence(SwiftMtNameElement name, AbstractMT message) {
        if (!name.isSequenceList()) {
            return message.getSequence(name.getSequence());
        }

        List<SwiftTagListBlock> blocks = message.getSequenceList(name.getSequence());
        SwiftMtFieldDescriptor fieldDescriptor = fieldDescriptors.ensureFieldDescriptor(name);
        SwiftMtCursor cursor = cursorsContainer.ensureCursor(fieldDescriptor, blocks.size(), null);

        return blocks.get(cursor.getCurrentPosition());
    }

    private SwiftTagListBlock readBlock(SwiftMtNameElement name, SwiftTagListBlock block) {
        if (block == null) {
            return null;
        }

        String blockName = name.getBlock();
        if (StringUtils.isEmpty(blockName)) {
            return block;
        }

        if (!name.isBlockList()) {
            return block.getSubBlock(blockName);
        }

        List<SwiftTagListBlock> subBlocks = block.getSubBlocks(blockName);
        SwiftMtCursor parentCursor = ensureParentCursor(name);

        SwiftMtFieldDescriptor fieldDescriptor = fieldDescriptors.ensureFieldDescriptor(name);
        SwiftMtCursor cursor = cursorsContainer.ensureCursor(fieldDescriptor, subBlocks.size(), parentCursor);

        return subBlocks.get(cursor.getCurrentPosition());
    }

    private Field readTag(SwiftMtNameElement name, SwiftTagListBlock subBlock) {
        if (subBlock == null) {
            return null;
        }

        String tagName = name.getTag();
        String qualifierName = name.getQualifier();
        if (!name.isTagList()) {
            return StringUtils.isEmpty(qualifierName)
                    ? subBlock.getFieldByName(tagName)
                    : subBlock.getFieldByName(tagName, qualifierName);
        }

        Field[] tags;
        if (StringUtils.isEmpty(qualifierName)) {
            tags = subBlock.getFieldsByName(tagName);
        } else {
            List<? extends Field> tagsList = subBlock.getFieldsByName(tagName, qualifierName);
            tags = tagsList.toArray(new Field[tagsList.size()]);
        }

        SwiftMtCursor parentCursor = ensureParentCursor(name);

        SwiftMtFieldDescriptor fieldDescriptor = fieldDescriptors.ensureFieldDescriptor(name);
        SwiftMtCursor cursor = cursorsContainer.ensureCursor(fieldDescriptor, tags.length, parentCursor);

        return tags[cursor.getCurrentPosition()];
    }

    private Object readComponent(SwiftMtNameElement name, Class<?> cls, Field tag) {
        if (tag == null) {
            return null;
        }
        return tag.getComponentAs(name.getComponent(), cls);
    }

    private SwiftMtCursor ensureParentCursor(SwiftMtNameElement name) {
        SwiftMtNameElement parentName = fieldDescriptors.createParentName(name);
        SwiftMtFieldDescriptor parentFieldDescriptor = fieldDescriptors.ensureFieldDescriptor(parentName);
        return cursorsContainer.ensureCursor(parentFieldDescriptor);
    }
}
