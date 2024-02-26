package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.establishment.service.domain.InstitutionBasicInfoBo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentSummaryBo {

	private Integer id;

	private Short stateId;

	private InstitutionBasicInfoBo institution;

	private LocalDate date;

	private LocalTime hour;

	private String phonePrefix;

	private String phoneNumber;

	private String patientEmail;

	private String professionalFirstName;

	private String professionalMiddleNames;

	private String professionalLastName;

	private String professionalOtherLastNames;

	private String professionalNameSelfDetermination;

	private Integer authorPersonId;

	private LocalDateTime createdOn;

	public AppointmentSummaryBo(Integer id, Short stateId, Integer institutionId,
								String institutionName, LocalDate date, LocalTime hour,
								String phonePrefix, String phoneNumber, String patientEmail, String professionalFirstName,
								String professionalMiddleNames, String professionalLastName,
								String professionalOtherLastNames, String professionalNameSelfDetermination,
								Integer authorPersonId, LocalDateTime createdOn) {
		this.id = id;
		this.institution = new InstitutionBasicInfoBo(institutionId, institutionName);
		this.stateId = stateId;
		this.date = date;
		this.hour = hour;
		this.phonePrefix = phonePrefix;
		this.phoneNumber = phoneNumber;
		this.patientEmail = patientEmail;
		this.professionalFirstName = professionalFirstName;
		this.professionalMiddleNames = professionalMiddleNames;
		this.professionalLastName = professionalLastName;
		this.professionalOtherLastNames = professionalOtherLastNames;
		this.professionalNameSelfDetermination = professionalNameSelfDetermination;
		this.authorPersonId = authorPersonId;
		this.createdOn = createdOn;
	}

}
