package net.pladema.imagenetwork.application.exception;

import lombok.Getter;

@Getter
public enum StudyExceptionEnum {
	PAC_SERVER_NOT_FOUND("El servidor PAC indicado como %s, o no se encuentra registrado o no existe en el sistema. Por favor verifique los datos de su servidor PAC."),
	STUDYINSTANCEUID_NOT_FOUND("El estudio identificado por %s, no se encuentra registrado en el sistema. Es posible que aún no se haya almacenado en un PAC Global en la nube para que esté disponible."),
	TOKEN_INVALID("El token '%s' asociado al estudio '%s', no es válido. El token o expiró, o se encuentra mal formado y no fue provisto por la app.");

	private final String message;

	StudyExceptionEnum(String message) {
		this.message = message;
	}
}
