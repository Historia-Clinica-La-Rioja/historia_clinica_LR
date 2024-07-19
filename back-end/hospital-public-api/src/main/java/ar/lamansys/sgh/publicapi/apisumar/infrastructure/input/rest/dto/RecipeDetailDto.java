package ar.lamansys.sgh.publicapi.apisumar.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@ToString
@Builder
@Getter
public class RecipeDetailDto {
	String institution;
	String operativeUnit;
	String lender;
	String lenderIdentificationNumber;
	Timestamp attentionDate;
	String patientIdentificationNumber;
	String patientName;
	String patientSex;
	String patientGender;
	String patientSelfPerceivedName;
	Date patientBirthDate;
	String patientAgeTurn;
	String patientAge;
	String ethnicity;
	String medicalCoverage;
	String address;
	String location;
	String instructionLevel;
	String workSituation;
	String medication;
	String relatedProblem;
	String evolution;
}
