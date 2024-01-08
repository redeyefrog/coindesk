package org.redeyefrog.exception;

public class CoinDeskRuntimeException extends RuntimeException {

    public CoinDeskRuntimeException(Throwable cause) {
        super(cause.getMessage(), cause, true, false);
    }

}
