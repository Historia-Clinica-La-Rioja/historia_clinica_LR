package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientPrescriptionDto {
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
	String country;
	String province;
	String department;
	String city;
	String street;
	String streetNumber;
}
