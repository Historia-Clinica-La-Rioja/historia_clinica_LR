package net.pladema.provincialreports.nursingreports.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class NursingHospitalizationConsultationDetail {

	private String institution;

	private String lastName;

	private String completeName;

	private String gender;

	private String identification;

	private String professional;

	private String licenseNumber;

	private String entryDate;

	private String probableDischargeDate;

	private String bed;

	private String categoryBed;

	private String roomName;

	private String sector;

	private String dischargeDate;

	private String procedures;

	private String vitalSign;

}
