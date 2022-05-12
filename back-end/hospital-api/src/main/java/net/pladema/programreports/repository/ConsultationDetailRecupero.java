package net.pladema.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ConsultationDetailRecupero {

	private String operativeUnit;

	private String provider;

	private String providerDni;

	private String attentionDate;

	private String consultationNumber;

	private String patientDni;

	private String patientName;

	private String gender;

	private String selfPerceivedGender;

	private String selfPerceivedName;

	private String birthDate;

	private String ageTurn;

	private String ageToday;

	private String ethnicity;

	private String medicalCoverage;

	private String address;

	private String location;

	private String educationLevel;

	private String occupation;

	private String systolicBloodPressure;

	private String diastolicBloodPressure;

	private String meanArterialPressure;

	private String temperature;

	private String heartRate;

	private String respirationRate;

	private String oxygenSaturationHemoglobin;

	private String height;

	private String weight;

	private String bmi;

	private String reasons;

	private String procedures;

	private String problems;

	private String medication;

	private String evolution;

}
