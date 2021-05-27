package ar.lamansys.odontology.application.odontogram.exception;

import ar.lamansys.odontology.application.exception.OdontologyException;

public class ToothNotFoundException extends OdontologyException {
    public ToothNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
