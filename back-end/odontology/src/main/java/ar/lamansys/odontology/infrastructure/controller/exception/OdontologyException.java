package ar.lamansys.odontology.infrastructure.controller.exception;

import lombok.Getter;

@Getter
public class OdontologyException extends Exception {

    public OdontologyException(String errorMessage) {
        super(errorMessage);
    }
}