package net.pladema.snvs.application.ports.event.exceptions;

import lombok.Getter;

@Getter
public class SnvsStorageException extends Exception {

    private SnvsStorageEnumException code;

    public SnvsStorageException(SnvsStorageEnumException code, String message) {
        super(message);
        this.code = code;
    }
}

