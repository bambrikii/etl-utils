package org.bambrikii.etl.model.transformer.adapters.pojo.io;

public class ReadStatus {
    private final ReadStatusEnum status;
    private final String message;

    public ReadStatus(ReadStatusEnum status, String message) {
        this.status = status;
        this.message = message;
    }

    public ReadStatusEnum getStatus(){
        return status;
    }

    public String getMessage() {
        return message;
    }
}
