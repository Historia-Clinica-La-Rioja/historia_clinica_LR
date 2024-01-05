package net.pladema.provincialreports.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SumarOdontologicoConsultationDetail {

	private String institution;

	private String operativeUnit;

	private String provider;

	private String providerDni;

	private String attentionDate;

	private String patientDni;

	private String patientName;

	private String gender;

	private String birthDate;

	private String ageTurn;

	private String medicalCoverage;

	private String indexCpo;

	private String indexCeo;

	private String address;

	private String location;

	private String reasons;

	private String procedures;

	private String problems;

}
