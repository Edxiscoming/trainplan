package org.railway.com.trainplan.exceptions;

/**
 * Created by speeder on 2014/5/27.
 */
public class WrongCheckTypeException extends WrongDataException {
    public WrongCheckTypeException() {
        super();
    }

    public WrongCheckTypeException(String message) {
        super(message);
    }

    public WrongCheckTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongCheckTypeException(Throwable cause) {
        super(cause);
    }

    protected WrongCheckTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
