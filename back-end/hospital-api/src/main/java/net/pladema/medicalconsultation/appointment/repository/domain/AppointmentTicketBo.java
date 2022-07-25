package net.pladema.medicalconsultation.appointment.repository.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTicketBo {

	private String institution;

	private String dni;

	private String patientLastName;

	private String patientOtherLastNames;

	private String patientFistName;

	private String patientMiddleNames;

	private String medicalCoverage;

	private LocalDate date;

	private LocalTime hour;

	private String doctorsOffice;

	private String doctorLastName;

	private String doctorOtherLastNames;

	private String doctorFistName;

	private String doctorMiddleNames;


	public String getPatientFullName(){
		String fullName = patientLastName;
		if(!(patientOtherLastNames == null || patientOtherLastNames.isBlank()))
			fullName += " " + patientOtherLastNames;
		fullName += " " + patientFistName;
		if(!(patientMiddleNames == null || patientMiddleNames.isBlank()))
			fullName += " " + patientMiddleNames;
		return fullName;
	}

	public String getDoctorFullName(){
		String fullName = doctorLastName;
		if(!(doctorOtherLastNames == null || doctorOtherLastNames.isBlank()))
			fullName += " " + doctorOtherLastNames;
		fullName += " " + doctorFistName;
		if(!(doctorMiddleNames == null || doctorMiddleNames.isBlank()))
			fullName += " " + doctorMiddleNames;
		return fullName;
	}

}
