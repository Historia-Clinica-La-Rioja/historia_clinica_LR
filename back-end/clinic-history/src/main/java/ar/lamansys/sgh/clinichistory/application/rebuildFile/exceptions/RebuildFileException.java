package ar.lamansys.sgh.clinichistory.application.rebuildFile.exceptions;

import lombok.Getter;

@Getter
public class RebuildFileException extends RuntimeException {

    private final RebuildFileEnumException code;

    public RebuildFileException(RebuildFileEnumException code, String message) {
        super(message);
        this.code = code;
    }
}