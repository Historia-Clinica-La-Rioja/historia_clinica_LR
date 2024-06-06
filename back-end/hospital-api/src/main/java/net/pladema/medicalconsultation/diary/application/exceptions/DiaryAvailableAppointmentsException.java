package net.pladema.medicalconsultation.diary.application.exceptions;

import lombok.Getter;

@Getter
public class DiaryAvailableAppointmentsException extends RuntimeException {

	public final DiaryAvailableAppointmentsExceptionEnum code;

	public DiaryAvailableAppointmentsException(DiaryAvailableAppointmentsExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
