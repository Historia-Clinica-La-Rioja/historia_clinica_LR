package net.pladema.medicalconsultation.appointment.service.domain;

import ar.lamansys.sgh.clinichistory.domain.ips.SnomedBo;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class EmptyAppointmentBo {

	private LocalDate date;
	private Integer diaryId;
	private LocalTime hour;
	private Integer openingHoursId;
	private boolean overturnMode;
	private Integer patientId;
	private String doctorsOfficeDescription;
	private String clinicalSpecialtyName;
	private String alias;
	private String doctorFullName;
	private Boolean includeNameSelfDetermination = AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS.isActive();
	private SnomedBo practice;

	public EmptyAppointmentBo(LocalDate date, Integer diaryId, LocalTime hour, Integer openingHoursId, boolean overturnMode, Integer patientId, String doctorsOfficeDescription, String clinicalSpecialtyName,String alias) {
		this.date = date;
		this.diaryId = diaryId;
		this.hour = hour;
		this.openingHoursId = openingHoursId;
		this.overturnMode = overturnMode;
		this.patientId = patientId;
		this.doctorsOfficeDescription = doctorsOfficeDescription;
		this.clinicalSpecialtyName = clinicalSpecialtyName;
		this.alias = alias;
		this.doctorFullName=null;
	}

	public EmptyAppointmentBo(String doctorLastName,
							  String doctorOtherLastNames, String doctorFirstName, String doctorMiddleNames, String nameSelfDetermination) {
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
