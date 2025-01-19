package ar.lamansys.sgh.publicapi.apisumar.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ImmunizationsDetailBo {

	private String institution;

	private String operativeUnit;

	private String lender;

	private String lenderIdentificationNumber;

	private Timestamp attentionDate;

	private String patientIdentificationNumber;

	private String patientName;

	private String patientSex;

	private String patientGender;

	private String patientSelfPerceivedName;

	private Date patientBirthDate;

	private String patientAgeTurn;

	private String patientAge;

	private String ethnicity;

	private String medicalCoverage;

	private String address;

	private String location;

	private String instructionLevel;

	private String workSituation;

	private String vaccine;

	private String dosage;

	private String lotNumber;

	private String note;

	private String scheme;

	private String vaccineScheme;

	private String applicationCondition;

	private String evolution;

}
