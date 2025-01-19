package net.pladema.clinichistory.outpatient.createoutpatient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OutpatientPatientBo {

	private Integer id;

	private Integer personId;

	private Short identificationType;

	private String identificationNumber;
	
	private Short genderId;

}
