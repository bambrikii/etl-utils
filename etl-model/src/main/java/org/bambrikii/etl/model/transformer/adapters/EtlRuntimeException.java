package org.bambrikii.etl.model.transformer.adapters;

public class EtlRuntimeException extends RuntimeException {
    public EtlRuntimeException(String message) {
        super(message);
    }

    public EtlRuntimeException(String message, Throwable ex) {
        super(message, ex);
    }
}
