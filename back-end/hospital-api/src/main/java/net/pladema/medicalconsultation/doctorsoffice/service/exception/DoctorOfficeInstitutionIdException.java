package net.pladema.medicalconsultation.doctorsoffice.service.exception;

import lombok.Getter;

@Getter
public class DoctorOfficeInstitutionIdException extends RuntimeException {

	private final DoctorOfficeEnumException code;

	public DoctorOfficeInstitutionIdException(DoctorOfficeEnumException code, String message) {
		super(message);
		this.code = code;
	}
}
