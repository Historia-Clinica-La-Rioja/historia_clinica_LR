package net.pladema.imagenetwork.application.exception;

import lombok.Getter;
import net.pladema.imagenetwork.domain.exception.EStudyException;

@Getter
public class StudyException extends RuntimeException {

	private final EStudyException code;

	public StudyException(EStudyException code, String message) {
		super(message);
		this.code = code;
	}

}