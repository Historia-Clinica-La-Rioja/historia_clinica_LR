package net.pladema.reports.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@ToString
@Getter
@AllArgsConstructor
public class NominalAppointmentDetailBo {

	private String province;

	private String department;

	private String sisaCode;

	private String institution;

	private String hierarchicalUnitType;

	private String hierarchicalUnitAlias;

	private String patientNames;

	private String patientSurname;

	private String patientSelfPerceivedName;

	private String identificationType;

	private String identificationNumber;

	private LocalDate birthDate;

	private String selfPerceivedGender;

	private LocalDate appointmentDate;

	private LocalTime appointmentHour;

	private String appointmentState;

	private String address;

	private String phoneNumber;

	private String email;

	private String coverageName;

	private String affiliateNumber;

	private String clinicalSpecialty;

	private String professionalName;

	private String diagnoses;

	private String issuerAppointmentFullName;

}
