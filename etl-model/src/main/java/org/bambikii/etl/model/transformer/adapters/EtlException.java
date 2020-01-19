package org.bambikii.etl.model.transformer.adapters;

public class EtlException extends Exception {
    public EtlException(String message, Throwable ex) {
        super(message, ex);
    }
}
