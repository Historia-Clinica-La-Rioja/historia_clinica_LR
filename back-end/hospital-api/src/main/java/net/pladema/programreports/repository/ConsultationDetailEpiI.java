package net.pladema.programreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ConsultationDetailEpiI {

	private String patientFullName;

	private String coding;

	private String birthDate;

	private String gender;

	private String startDate;

	private String department;

	private String address;

	private String cie10Codes;

	private String identificationNumber;

	private String problems;
}
