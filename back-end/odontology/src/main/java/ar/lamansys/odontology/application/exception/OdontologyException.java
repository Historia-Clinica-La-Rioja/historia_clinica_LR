package ar.lamansys.odontology.application.exception;

import lombok.Getter;

@Getter
public class OdontologyException extends RuntimeException {

    public OdontologyException(String errorMessage) {
        super(errorMessage);
    }
}