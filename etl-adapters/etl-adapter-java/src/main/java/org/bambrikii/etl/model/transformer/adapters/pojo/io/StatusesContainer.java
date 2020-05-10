package org.bambrikii.etl.model.transformer.adapters.pojo.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusesContainer {
    private final List<ReadStatus> statuses = new ArrayList<>();
    private final List<ReadStatus> failedStatuses = new ArrayList<>();

    public void addStatus(ReadStatusEnum type, String message) {
        ReadStatus status = new ReadStatus(type, message);
        statuses.add(status);
        switch (type) {
            case LIST_READ_FAILED:
                failedStatuses.add(status);
                break;
        }
    }

    public void clearStatuses() {
        statuses.clear();
        failedStatuses.clear();
    }

    public List<ReadStatus> getStatuses() {
        return Collections.unmodifiableList(statuses);
    }

    public boolean isValid() {
        return failedStatuses.size() == 0;
    }
}
