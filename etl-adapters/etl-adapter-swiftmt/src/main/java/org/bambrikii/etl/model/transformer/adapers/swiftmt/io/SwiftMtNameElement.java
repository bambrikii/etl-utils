package org.bambrikii.etl.model.transformer.adapers.swiftmt.io;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
class SwiftMtNameElement {
    private String sequence;
    private boolean sequenceList;

    private String block;
    private boolean blockList;

    private String tag;
    private boolean tagList;

    private String qualifier;

    private Integer component;

    private SwiftMtNameType type;
}
