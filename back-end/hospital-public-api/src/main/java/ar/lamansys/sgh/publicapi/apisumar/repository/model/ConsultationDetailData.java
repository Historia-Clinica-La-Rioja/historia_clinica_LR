package ar.lamansys.sgh.publicapi.apisumar.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationDetailData {

	private String institution;

	private String origin;

	private String operativeUnit;

	private String lender;

	private String lenderIdentificationNumber;

	private String attentionDate;

	private String patientIdentificationNumber;

	private String patientName;

	private String patientSex;

	private String patientGender;

	private String patientSelfPerceivedName;

	private String patientBirthDate;

	private String patientAgeTurn;

	private String patientAge;

	private String ethnicity;

	private String medicalCoverage;

	private String address;

	private String location;

	private String instructionLevel;

	private String workSituation;

	private String systolicPressure;

	private String diastolicPressure;

	private String meanArterialPressure;

	private String temperature;

	private String heartRate;

	private String respiratoryRate;

	private String bloodOxygenSaturation;

	private String height;

	private String weight;

	private String bmi;

	private String headCircumference;

	private String reasons;

	private String procedures;

	private String dentalProcedures;

	private String cpo;

	private String ceo;

	private String problems;

	private String medication;

	private String evolution;

}
