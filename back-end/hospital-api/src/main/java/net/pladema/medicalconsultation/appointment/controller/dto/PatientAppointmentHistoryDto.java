package net.pladema.medicalconsultation.appointment.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatientAppointmentHistoryDto {

	private DateTimeDto dateTime;

	private String institution;

	private String city;

	private String firstName;

	private String lastName;

	private String clinicalSpecialty;

	private String practice;

	private String service;

	private Short statusId;

	private String doctorsOffice;

}
