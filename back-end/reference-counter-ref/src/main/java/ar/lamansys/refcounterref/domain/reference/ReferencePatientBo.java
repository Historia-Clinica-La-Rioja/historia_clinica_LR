package ar.lamansys.refcounterref.domain.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReferencePatientBo {

	private Integer patientId;

	private String patientFullName;

	private String identificationNumber;

	private String identificationType;

	private String phonePrefix;

	private String phoneNumber;

	private String email;

	public ReferencePatientBo (Integer patientId, String patientFullName, String identificationNumber,
							   String identificationType, String email) {
		this.patientId = patientId;
		this.patientFullName = patientFullName;
		this.identificationNumber = identificationNumber;
		this.identificationType = identificationType;
		this.email = email;
	}

}
