package net.pladema.establishment.application.bed.exceptions;

import lombok.Getter;
import net.pladema.establishment.domain.bed.exceptions.BedRelocationEnumException;

@Getter
public class BedRelocationException extends RuntimeException {

    private final BedRelocationEnumException code;

    public BedRelocationException(BedRelocationEnumException code, String message) {
        super(message);
        this.code = code;
    }
}