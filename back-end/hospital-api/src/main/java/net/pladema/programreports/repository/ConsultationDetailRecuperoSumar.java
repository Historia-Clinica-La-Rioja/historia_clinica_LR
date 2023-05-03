package net.pladema.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ConsultationDetailRecuperoSumar {

	private String institution;

	private Integer clinicalSpecialtyId;

	private String clinicalSpecialty;

	private Integer professionalId;

	private String provider;

	private String providerDni;

	private String attentionDate;

	private String hour;

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

	private String headCircunference;

	private String reasons;

	private String procedures;

	private String problems;

	private String medication;

	private String evolution;

	public ConsultationDetailRecuperoSumar(String institution, Integer clinicalSpecialtyId, String clinicalSpecialty, Integer professionalId, String provider, String providerDni, String attentionDate, String hour, String consultationNumber, String patientDni, String patientName, String gender, String selfPerceivedGender, String selfPerceivedName, String birthDate, String ageTurn, String ageToday, String ethnicity, String medicalCoverage, String address, String location, String educationLevel, String occupation, String systolicBloodPressure, String diastolicBloodPressure, String meanArterialPressure, String temperature, String heartRate, String respirationRate, String oxygenSaturationHemoglobin, String height, String weight, String headCircunference, String reasons, String procedures, String problems, String medication, String evolution) {
		this.institution = institution;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.clinicalSpecialty = clinicalSpecialty;
		this.professionalId = professionalId;
		this.provider = provider;
		this.providerDni = providerDni;
		this.attentionDate = attentionDate;
		this.hour = hour;
		this.consultationNumber = consultationNumber;
		this.patientDni = patientDni;
		this.patientName = patientName;
		this.gender = gender;
		this.selfPerceivedGender = selfPerceivedGender;
		this.selfPerceivedName = selfPerceivedName;
		this.birthDate = birthDate;
		this.ageTurn = ageTurn;
		this.ageToday = ageToday;
		this.ethnicity = ethnicity;
		this.medicalCoverage = medicalCoverage;
		this.address = address;
		this.location = location;
		this.educationLevel = educationLevel;
		this.occupation = occupation;
		this.systolicBloodPressure = systolicBloodPressure;
		this.diastolicBloodPressure = diastolicBloodPressure;
		this.meanArterialPressure = meanArterialPressure;
		this.temperature = temperature;
		this.heartRate = heartRate;
		this.respirationRate = respirationRate;
		this.oxygenSaturationHemoglobin = oxygenSaturationHemoglobin;
		this.height = height;
		this.weight = weight;
		this.headCircunference = headCircunference;
		this.reasons = reasons;
		this.procedures = procedures;
		this.problems = problems;
		this.medication = medication;
		this.evolution = evolution;


	}
	public ConsultationDetailRecuperoSumar( String bmiResult){
		this.bmi = bmiResult;
	}
}
