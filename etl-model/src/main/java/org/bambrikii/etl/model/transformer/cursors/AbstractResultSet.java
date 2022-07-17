package org.bambrikii.etl.model.transformer.cursors;

import java.util.List;

public abstract class AbstractResultSet<
        S,
        D extends AbstractFieldDescriptorsContainer,
        C extends AbstractCursorsContainer
        > {
    protected final S source;
    protected final D fieldDescriptors;
    protected final C cursorsContainer;
    protected final StatusesContainer statusesContainer;

    public AbstractResultSet(S source, D fieldDescriptors, C cursorsContainer) {
        this.source = source;
        this.fieldDescriptors = fieldDescriptors;
        this.cursorsContainer = cursorsContainer;
        statusesContainer = new StatusesContainer();
    }

    public abstract <T> T getObject(String name, Class<T> valueClass);

    public boolean next() {
        statusesContainer.clearStatuses();
        return cursorsContainer.next();
    }

    public boolean isStatusValid() {
        return statusesContainer.isValid();
    }

    public List<ReadStatus> statuses() {
        return statusesContainer.getStatuses();
    }
}
