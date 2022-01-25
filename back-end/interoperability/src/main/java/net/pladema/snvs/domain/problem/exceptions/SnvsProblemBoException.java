package net.pladema.snvs.domain.problem.exceptions;

import lombok.Getter;

@Getter
public class SnvsProblemBoException extends RuntimeException {

    private SnvsProblemBoEnumException code;

    public SnvsProblemBoException(SnvsProblemBoEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

