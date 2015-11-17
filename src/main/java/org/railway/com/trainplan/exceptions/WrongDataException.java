package org.railway.com.trainplan.exceptions;

/**
 * Created by speeder on 2014/5/23.
 */
public class WrongDataException extends DailyPlanCheckException {
    public WrongDataException() {
        super();
    }

    public WrongDataException(String message) {
        super(message);
    }

    public WrongDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongDataException(Throwable cause) {
        super(cause);
    }

    protected WrongDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
