package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.mt.AbstractMT;

public class SwiftMtResultSet {
    private final AbstractMT message;

    public SwiftMtResultSet(AbstractMT message) {
        this.message = message;
    }

    public <T> T getObject(String name, Class<T> cls) {
        String[] names = name.split(":");
        String sequenceName = names[0];
        String subBlockName = names[1];
        String tagName = names[2];
        String qualifierName = names[3];
        Integer componentNumber = Integer.parseInt(names[4]);
        SwiftTagListBlock block;
        /*
        if (sequenceName.length() == 1) {
            block = message.getSequence(sequenceName);
        } else {
            block = message.getSequenceList(sequenceName);
        }
        if (!StringUtils.isEmpty(subBlockName)) {
            block = block.getSubBlock(subBlockName);
        }
        Tag tag = block.getTagByName(tagName);
         */
// TODO:        block.getField
// TODO:       tag.asField().
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
