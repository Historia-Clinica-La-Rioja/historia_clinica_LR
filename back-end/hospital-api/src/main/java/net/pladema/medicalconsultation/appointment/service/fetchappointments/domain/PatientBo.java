package net.pladema.medicalconsultation.appointment.service.fetchappointments.domain;

import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class PatientBo  {

	@ToString.Include
	private final Integer id;

	@ToString.Include
	private final String firstName;

	@ToString.Include
	private final String lastName;

	@ToString.Include
	private final String identificationNumber;

	@ToString.Include
	private final Short genderId;

	public PatientBo(Integer id, String firstName, String lastName, String identificationNumber, Short genderId) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.identificationNumber = identificationNumber;
		this.genderId = genderId;
	}
}
