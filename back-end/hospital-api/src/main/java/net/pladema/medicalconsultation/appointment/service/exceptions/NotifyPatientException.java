package net.pladema.medicalconsultation.appointment.service.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotifyPatientException extends Exception {
	public final Integer sectorId;
}
