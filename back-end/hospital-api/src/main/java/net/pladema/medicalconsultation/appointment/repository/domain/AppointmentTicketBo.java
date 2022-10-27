package net.pladema.medicalconsultation.appointment.repository.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppointmentTicketBo {

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

	private String doctorsOffice;

	private String doctorLastName;

	private String doctorOtherLastNames;

	private String doctorFirstName;

	private String doctorMiddleNames;

	private String doctorNameSelfDetermination;

	private boolean includeNameSelfDetermination;

	public AppointmentTicketBo (String institution, String documentNumber, String patientLastName, String patientOtherLastNames,
								String patientFirstName, String patientMiddleNames, String patientNameSelfDetermination, String medicalCoverage,
								String medicalCoverageAcronym, LocalDate date, LocalTime hour, String doctorsOffice, String doctorLastName,
								String doctorOtherLastNames, String doctorFirstName, String doctorMiddleNames, String doctorNameSelfDetermination) {
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
		this.doctorsOffice = doctorsOffice;
		this.doctorLastName = doctorLastName;
		this.doctorOtherLastNames = doctorOtherLastNames;
		this.doctorFirstName = doctorFirstName;
		this.doctorMiddleNames = doctorMiddleNames;
		this.doctorNameSelfDetermination = doctorNameSelfDetermination;

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
		if(includeNameSelfDetermination && !(patientNameSelfDetermination == null || patientNameSelfDetermination.isBlank()))
			fullName += " " + patientNameSelfDetermination;
		else
			fullName += " " + patientFirstName;
		if(!(patientMiddleNames == null || patientMiddleNames.isBlank()))
			fullName += " " + patientMiddleNames;
		return fullName;
	}

	public String getDoctorFullName(){
		String fullName = doctorLastName;
		if(!(doctorOtherLastNames == null || doctorOtherLastNames.isBlank()))
			fullName += " " + doctorOtherLastNames;
		if(includeNameSelfDetermination && !(doctorNameSelfDetermination == null || doctorNameSelfDetermination.isBlank()))
			fullName += " " + doctorNameSelfDetermination;
		else
			fullName += " " + doctorFirstName;
		if(!(doctorMiddleNames == null || doctorMiddleNames.isBlank()))
			fullName += " " + doctorMiddleNames;
		return fullName;
	}

}
