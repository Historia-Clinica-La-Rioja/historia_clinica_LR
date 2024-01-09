package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class AppointmentBookingVo {

	private String professionalFirstName;

	private String professionalMiddleNames;

	private String professionalOtherLastNames;

	private String professionalLastName;

	private LocalDate date;

	private LocalTime hour;

	private String office;

	private List<String> specialties = new ArrayList<>();

	public AppointmentBookingVo(String professionalFirstName, String professionalMiddleNames, String professionalLastName, String professionalOtherLastNames, LocalDate date, LocalTime hour, String office, String specialty) {
		this.professionalFirstName = professionalFirstName;
		this.professionalMiddleNames = professionalMiddleNames;
		this.professionalOtherLastNames = professionalOtherLastNames;
		this.professionalLastName = professionalLastName;
		this.date = date;
		this.hour = hour;
		this.office = office;
		this.specialties.add(specialty);
	}
}
