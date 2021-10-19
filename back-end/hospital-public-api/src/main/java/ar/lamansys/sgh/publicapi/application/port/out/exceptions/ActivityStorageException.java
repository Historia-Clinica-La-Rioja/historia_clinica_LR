package ar.lamansys.sgh.publicapi.application.port.out.exceptions;

import lombok.Getter;

@Getter
public class ActivityStorageException extends RuntimeException {

    public final ActivityStorageExceptionEnum code;

    public ActivityStorageException(ActivityStorageExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }
}
