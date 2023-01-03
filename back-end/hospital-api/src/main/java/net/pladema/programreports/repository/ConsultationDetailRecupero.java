package net.pladema.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.checkerframework.checker.units.qual.C;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class ConsultationDetailRecupero {

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

	private String reasons;

	private String procedures;

	private String problems;

	private String medication;

	private String evolution;

	public ConsultationDetailRecupero(String institution,String clinicalSpecialty, String provider,String providerDni, String attentionDate,String hour,
									 String consultationNumber, String patientDni, String patientName, String gender,
									  String birthDate, String ageTurn,String ageToday, String medicalCoverage,String address, String location,
									  String bmi, String reasons, String procedures, String problems, String medication, String evolution) {
		this.institution = institution;
		this.clinicalSpecialty = clinicalSpecialty;
		this.provider = provider;
		this.providerDni = providerDni;
		this.attentionDate = attentionDate;
		this.hour = hour;
		this.consultationNumber = consultationNumber;
		this.patientDni = patientDni;
		this.patientName = patientName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.ageTurn = ageTurn;
		this.ageToday = ageToday;
		this.medicalCoverage = medicalCoverage;
		this.address = address;
		this.location = location;
		this.bmi = bmi;
		this.reasons = reasons;
		this.procedures = procedures;
		this.problems = problems;
		this.medication = medication;
		this.evolution = evolution;
	}
}
