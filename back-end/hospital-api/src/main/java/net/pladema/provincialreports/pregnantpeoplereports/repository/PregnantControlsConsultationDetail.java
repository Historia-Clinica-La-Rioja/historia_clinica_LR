package net.pladema.provincialreports.pregnantpeoplereports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PregnantControlsConsultationDetail {

	private String patientIdentificationNumber;

	private String patientLastName;

	private String patientFirstName;

	private String patientBirthDate;

	private String patientAgeAtAttention;

	private String patientAddress;

	private String phoneNumber;

	private String patientLocation;

	private String numberConsultations;

	private String consultations;

}
