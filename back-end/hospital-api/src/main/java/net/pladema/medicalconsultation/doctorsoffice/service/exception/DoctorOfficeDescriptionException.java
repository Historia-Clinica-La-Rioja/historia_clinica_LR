package net.pladema.medicalconsultation.doctorsoffice.service.exception;

import lombok.Getter;

@Getter
public class DoctorOfficeDescriptionException extends RuntimeException {

	private final DoctorOfficeEnumException code;

	public DoctorOfficeDescriptionException(DoctorOfficeEnumException code, String message) {
		super(message);
		this.code = code;
	}
}
