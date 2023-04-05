package net.pladema.imagenetwork.application.exception;

import lombok.Getter;

@Getter
public enum StudyExceptionEnum {
	STUDYINSTANCEUID_NOT_FOUND("El estudio identificado por %s, no se encuentra registrado en el sistema. Es posible que aún no se haya almacenado en un PAC Global en la nube para que esté disponible.");

	private final String message;

	StudyExceptionEnum(String message) {
		this.message = message;
	}
}
