package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class PatientAppointmentHistoryBo {

	private LocalDate date;

	private LocalTime time;

	private String institution;

	private String city;

	private Integer doctorPersonId;

	private String doctorName;

	private String clinicalSpecialty;

	private String practice;

	private String service;

	private Short statusId;

	public PatientAppointmentHistoryBo(LocalDate date, LocalTime time, String institution, String city, Integer doctorPersonId, String clinicalSpecialty, String practice,
									   String service, Short statusId) {
		this.date = date;
		this.time = time;
		this.institution = institution;
		this.city = city;
		this.doctorPersonId = doctorPersonId;
		this.clinicalSpecialty = clinicalSpecialty;
		this.practice = practice;
		this.service = service;
		this.statusId = statusId;
	}

}
