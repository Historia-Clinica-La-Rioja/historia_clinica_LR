package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Getter
@Setter
@ToString
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
}
