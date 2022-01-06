package ar.lamansys.refcounterref.application.deletefiles.exceptions;

import lombok.Getter;

@Getter
public class DeleteFilesException extends RuntimeException {

    public final DeleteFilesExceptionEnum code;

    public DeleteFilesException(DeleteFilesExceptionEnum code, String message) {
        super(message);
        this.code = code;
    }

}