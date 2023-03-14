package net.pladema.medicalconsultation.appointment.repository.domain;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
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

	private Boolean includeNameSelfDetermination = AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS.isActive();

	public AppointmentShortSummaryBo(String institution, LocalDate date, LocalTime hour, String doctorLastName,
									 String doctorOtherLastNames, String doctorFirstName, String doctorMiddleNames, String nameSelfDetermination) {
		this.institution = institution;
		this.date = date;
		this.hour = hour;
		this.doctorFullName = this.getDoctorFullName(doctorLastName, doctorOtherLastNames, doctorFirstName, doctorMiddleNames, nameSelfDetermination);
	}

	public String getDoctorFullName(String doctorLastName, String doctorOtherLastNames,
									String doctorFirstName, String doctorMiddleNames, String nameSelfDetermination){
		String fullName = doctorLastName;
		if(!(doctorOtherLastNames == null || doctorOtherLastNames.isBlank()))
			fullName += " " + doctorOtherLastNames;
		if(includeNameSelfDetermination && !(nameSelfDetermination == null || nameSelfDetermination.isBlank()))
			fullName += " " + nameSelfDetermination;
		else
			fullName += " " + doctorFirstName;
		if(!(includeNameSelfDetermination) && !(doctorMiddleNames == null || doctorMiddleNames.isBlank()))
			fullName += " " + doctorMiddleNames;
		return fullName;
	}

}
