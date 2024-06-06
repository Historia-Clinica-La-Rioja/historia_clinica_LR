package net.pladema.reports.imageNetworkProductivity.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class CellDataBo {

	private String patientLastName;

	private String patientFirstName;

	private String patientSelfDeterminationName;

	private String identificationType;

	private String identificationNumber;

	private String selfDeterminationGender;

	private String patientStreetName;

	private String patientStreetNumber;

	private LocalDate patientBirthDate;

	private String phonePrefix;

	private String phoneNumber;

	private String patientEmail;

	private String medicalCoverageName;

	private String affiliateNumber;

	private LocalDate appointmentDate;

	private LocalTime appointmentHour;

	private String problem;

	private String modality;

	private String practice;

	private Integer technicianPersonId;

	private Integer informerPersonId;

	private String transcribedRequestProfessional;

	private Integer nonTranscribedRequestProfessionalPersonId;

	private String sourceType;

}
