package net.pladema.imagenetwork.derivedstudies.application.exception;

import lombok.Getter;
import net.pladema.imagenetwork.derivedstudies.domain.exception.EMoveStudiesException;

@Getter
public class MoveStudiesException extends RuntimeException {

    private final EMoveStudiesException code;

    public MoveStudiesException(EMoveStudiesException code, String message) {
        super(message);
        this.code = code;
    }
}
