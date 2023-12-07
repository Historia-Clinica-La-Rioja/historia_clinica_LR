package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class PatientPrescriptionBo {
	String name;
	String lastName;
	String selfPerceivedName;
	String dniSex;
	String gender;
	LocalDate birthDate;
	String identificationType;
	String identificationNumber;
	String medicalCoverage;
	String medicalCoverageCuit;
	String medicalCoveragePlan;
	String affiliateNumber;
}
