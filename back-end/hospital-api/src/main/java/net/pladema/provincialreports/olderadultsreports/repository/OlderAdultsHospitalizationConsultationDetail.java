package net.pladema.provincialreports.olderadultsreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OlderAdultsHospitalizationConsultationDetail {

	private String institution;

	private String lastName;

	private String name;

	private String gender;

	private String identification;

	private String birthDate;

	private String ageTurn;

	private String ageToday;

	private String phone;

	private String entrance;

	private String probableEnablement;

	private String bed;

	private String category;

	private String room;

	private String sector;

	private String medicalClearance;

	private String problem;

}
