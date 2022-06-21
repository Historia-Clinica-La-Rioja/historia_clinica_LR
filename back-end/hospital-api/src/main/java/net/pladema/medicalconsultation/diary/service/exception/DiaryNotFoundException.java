package net.pladema.medicalconsultation.diary.service.exception;

import lombok.Getter;

@Getter
public class DiaryNotFoundException extends RuntimeException {

	private final DiaryNotFoundEnumException code;

	public DiaryNotFoundException(DiaryNotFoundEnumException code, String errorMessage) {
		super(errorMessage);
		this.code = code;
	}
}
