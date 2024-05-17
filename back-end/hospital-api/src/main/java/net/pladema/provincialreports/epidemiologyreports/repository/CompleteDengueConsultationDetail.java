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
public class CompleteDengueConsultationDetail {

	private String isFalsePositive;

	private String origin;

	private String operativeUnit;

	private String patientIdentificationNumber;

	private String patientLastName;

	private String patientFirstName;

	private String patientSex;

	private String patientBirthDate;

	private String patientAge;

	private String attentionHour;

	private String medicalCoverage;

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

	private String reason;

	private String problems;

	private String procedures;

	private String evolution;

}
