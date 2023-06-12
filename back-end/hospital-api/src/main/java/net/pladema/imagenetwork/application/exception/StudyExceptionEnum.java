package net.pladema.imagenetwork.application.exception;

import lombok.Getter;

@Getter
public enum StudyExceptionEnum {
	VIEWER_URL_NOT_DEFINED("La URL del visualizador web no fue configurada desde las propiedades del sistema."),
	PAC_SERVER_NOT_FOUND("El servidor PAC, o no se encuentra registrado o no existe en el sistema. Por favor verifique los datos."),
	STUDYINSTANCEUID_NOT_FOUND("El estudio identificado por '%s', no se encuentra registrado en el sistema. Es posible que aún no se haya almacenado en un PAC Global en la nube para que esté disponible."),
	TOKEN_INVALID("El token enviado para acceder al estudio '%s', no es válido. Expiró o se encuentra mal formado y no fue provisto por la app.");

	private final String message;

	StudyExceptionEnum(String message) {
		this.message = message;
	}
}
