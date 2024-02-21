package net.pladema.medicalconsultation.unsitisfiedDemand.application.exception;

import lombok.Getter;
import net.pladema.medicalconsultation.unsitisfiedDemand.domain.exception.EUnsatisfiedAppointmentDemandCode;

@Getter
public class UnsatisfiedAppointmentDemandException extends RuntimeException {

	private static final long serialVersionUID = -7564994259798540601L;

	private final EUnsatisfiedAppointmentDemandCode code;

	public UnsatisfiedAppointmentDemandException(EUnsatisfiedAppointmentDemandCode code, String message) {
		super(message);
		this.code = code;
	}

}
