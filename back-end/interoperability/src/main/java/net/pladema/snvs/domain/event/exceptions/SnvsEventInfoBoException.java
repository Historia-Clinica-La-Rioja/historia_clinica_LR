package net.pladema.snvs.domain.event.exceptions;

import lombok.Getter;

@Getter
public class SnvsEventInfoBoException extends Exception {

    private SnvsEventInfoBoEnumException code;

    public SnvsEventInfoBoException(SnvsEventInfoBoEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

