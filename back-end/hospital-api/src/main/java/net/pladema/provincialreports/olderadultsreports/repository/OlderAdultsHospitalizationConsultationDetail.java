package net.pladema.provincialreports.olderadultsreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OlderAdultsHospitalizationConsultationDetail {

	private String institution;

	private String patientLastNames;

	private String patientNames;

	private String gender;

	private String identification;

	private String birthDate;

	private String ageTurn;

	private String ageToday;

	private String phone;

	private String entryDate;

	private String probableDischargeDate;

	private String bed;

	private String category;

	private String room;

	private String sector;

	private String dischargeDate;

	private String problems;

}
