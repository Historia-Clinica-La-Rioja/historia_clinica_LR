package ar.lamansys.pac.infrastructure.input.rest.exceptions;

import lombok.Getter;

@Getter
public class StudyAccessException extends RuntimeException {

	private final StudyAccessExceptionEnum code;

	public StudyAccessException(StudyAccessExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
