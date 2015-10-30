package org.railway.com.trainplan.exceptions;

/**
 * Created by speeder on 2014/5/24.
 */
public class UnknownCheckTypeException extends DailyPlanCheckException {
    public UnknownCheckTypeException() {
        super();
    }

    public UnknownCheckTypeException(String message) {
        super(message);
    }

    public UnknownCheckTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownCheckTypeException(Throwable cause) {
        super(cause);
    }

    protected UnknownCheckTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
