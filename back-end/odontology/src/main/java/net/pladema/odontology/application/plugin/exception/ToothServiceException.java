package net.pladema.odontology.application.plugin.exception;

import lombok.Getter;

@Getter
public class ToothServiceException extends Exception {

    public ToothServiceException( String mensajeError) {
        super(mensajeError);
    }
}