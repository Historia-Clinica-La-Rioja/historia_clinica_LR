package net.pladema.medicalconsultation.appointment.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentTicketImageBo {

	private String institution;

	private String documentNumber;

	private String patientLastName;

	private String patientOtherLastNames;

	private String patientFirstName;

	private String patientMiddleNames;

	private String patientNameSelfDetermination;

	private String medicalCoverage;

	private String medicalCoverageAcronym;

	private LocalDate date;

	private LocalTime hour;

	private String sectorName;

	private Integer serviceRequestId;

	private String studyDescription;


	public AppointmentTicketImageBo(String institution, String documentNumber, String patientLastName, String patientOtherLastNames,
									String patientFirstName, String patientMiddleNames, String patientNameSelfDetermination, String medicalCoverage,
									String medicalCoverageAcronym, LocalDate date, LocalTime hour, String sectorName, String studyDescription,
									Integer serviceRequestId) {
		this.institution = institution;
		this.documentNumber = documentNumber;
		this.patientLastName = patientLastName;
		this.patientOtherLastNames = patientOtherLastNames;
		this.patientFirstName = patientFirstName;
		this.patientMiddleNames = patientMiddleNames;
		this.patientNameSelfDetermination = patientNameSelfDetermination;
		this.medicalCoverage = medicalCoverage;
		this.medicalCoverageAcronym = medicalCoverageAcronym;
		this.date = date;
		this.hour = hour;
		this.sectorName = sectorName;
		this.studyDescription = studyDescription;
		this.serviceRequestId = serviceRequestId;
	}

	public String getMedicalCoverage() {
		if(medicalCoverageAcronym != null && !medicalCoverageAcronym.isBlank())
			return medicalCoverageAcronym;
		return medicalCoverage;
	}

	public String getPatientFullName(){
		String fullName = patientLastName;
		if(!(patientOtherLastNames == null || patientOtherLastNames.isBlank()))
			fullName += " " + patientOtherLastNames;
		if(!(patientNameSelfDetermination == null || patientNameSelfDetermination.isBlank()))
			fullName += " " + patientNameSelfDetermination;
		else {
			fullName += " " + patientFirstName;
			if (!(patientMiddleNames == null || patientMiddleNames.isBlank()))
				fullName += " " + patientMiddleNames;
		}
		return fullName;
	}

}
