package net.pladema.imagenetwork.application.exception;

import lombok.Getter;

@Getter
public class StudyException extends RuntimeException {

	private final StudyExceptionEnum code;

	public StudyException(StudyExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}