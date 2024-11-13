package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class StudyOrderPatientLocationBo {

	private String bedNumber;
	private String roomNumber;
	private String sector;
	private String location;

}
