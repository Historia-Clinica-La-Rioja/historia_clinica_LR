package net.pladema.medicalconsultation.doctorsoffice.service.exception;

import lombok.Getter;

@Getter
public class DoctorOfficeSectorIdException extends RuntimeException{

	private final DoctorOfficeEnumException code;

	public DoctorOfficeSectorIdException(DoctorOfficeEnumException code, String message) {
		super(message);
		this.code = code;
	}
}
