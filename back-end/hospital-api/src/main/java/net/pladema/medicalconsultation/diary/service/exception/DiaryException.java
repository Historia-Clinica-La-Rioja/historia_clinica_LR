package net.pladema.medicalconsultation.diary.service.exception;

import lombok.Getter;

@Getter
public class DiaryException extends RuntimeException {

	private final DiaryEnumException code;

	public DiaryException(DiaryEnumException code, String mensajeError) {
		super(mensajeError);
		this.code = code;
	}

}
