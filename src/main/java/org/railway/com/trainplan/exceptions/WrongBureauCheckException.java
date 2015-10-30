package org.railway.com.trainplan.exceptions;

/**
 * Created by speeder on 2014/5/24.
 */
public class WrongBureauCheckException extends WrongDataException {
    public WrongBureauCheckException() {
        super();
    }

    public WrongBureauCheckException(String message) {
        super(message);
    }

    public WrongBureauCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongBureauCheckException(Throwable cause) {
        super(cause);
    }

    protected WrongBureauCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
