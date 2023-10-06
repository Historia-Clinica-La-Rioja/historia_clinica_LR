package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class PatientAppointmentHistoryBo {

	private LocalDate date;

	private LocalTime time;

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
