package net.pladema.provincialreports.epidemiologyreports.repository;

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
public class DenguePatientControl {

	private String isFalsePositive;

	private String patientIdentificationNumber;

	private String patientLastName;

	private String patientFirstName;

	private String patientSex;

	private String patientBirthDate;

	private String patientAge;

	private String patientAddress;

	private String patientPhoneNumber;

	private String patientLocation;

}
