package net.pladema.clinichistory.requests.servicerequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StudyOrderPatientLocationDto {

	private String bedNumber;
	private String roomNumber;
	private String sector;

}
