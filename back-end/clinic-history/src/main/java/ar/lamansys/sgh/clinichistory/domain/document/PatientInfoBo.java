package ar.lamansys.sgh.clinichistory.domain.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientInfoBo{

	private Integer id;

	private Short genderId;

	private Short age;

	private String identificationType;

	private String identificationNumber;

	public PatientInfoBo(Integer patientId) {
		this.id = patientId;
	}

	public PatientInfoBo(Integer id, Short genderId, Short age){
		this.id = id;
		this.genderId = genderId;
		this.age = age;
	}


}