package net.pladema.provincialreports.olderadultsreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OlderAdultsOutpatientConsultationDetail {

	private String institution;

	private String lender;

	private String lenderDni;

	private String attentionDate;

	private String hour;

	private String consultationNumber;

	private String patientDni;

	private String patientName;

	private String gender;

	private String birthDate;

	private String ageTurn;

	private String ageToday;

	private String medicalCoverage;

	private String address;

	private String locationPatient;

	private String phoneNumber;

	private String problems;

}
