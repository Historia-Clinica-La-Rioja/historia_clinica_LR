package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentShortSummaryBo {

	private String institution;

	private LocalDate date;

	private LocalTime hour;

	private String doctorFullName;

	public AppointmentShortSummaryBo(String institution, LocalDate date, LocalTime hour, String doctorLastName,
									 String doctorOtherLastNames, String doctorFirstName, String doctorMiddleNames) {
		this.institution = institution;
		this.date = date;
		this.hour = hour;
		this.doctorFullName = this.getDoctorFullName(doctorLastName, doctorOtherLastNames, doctorFirstName, doctorMiddleNames);
	}

	public String getDoctorFullName(String doctorLastName, String doctorOtherLastNames,
									String doctorFirstName, String doctorMiddleNames){
		String fullName = doctorLastName;
		if(!(doctorOtherLastNames == null || doctorOtherLastNames.isBlank()))
			fullName += " " + doctorOtherLastNames;
		fullName += " " + doctorFirstName;
		if(!(doctorMiddleNames == null || doctorMiddleNames.isBlank()))
		fullName += " " + doctorMiddleNames;
		return fullName;
	}

}
