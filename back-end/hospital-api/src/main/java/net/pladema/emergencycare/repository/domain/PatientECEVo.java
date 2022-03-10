package net.pladema.emergencycare.repository.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.entity.Person;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PatientECEVo implements Serializable {

	private static final long serialVersionUID = 2213101236822587231L;

	private Integer id;

	private Integer patientMedicalCoverageId;

	private Short typeId;

	private PersonECEVo person;

	public PatientECEVo(Integer id, Integer patientMedicalCoverageId, Short typeId, Person person){
		this.id = id;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.typeId = typeId;
		if (person != null)
			this.person = new PersonECEVo(person);

	}

	public PatientECEVo(Integer id, Integer patientMedicalCoverageId, Short typeId, Person person, String nameSelfDetermination){
		this.id = id;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
		this.typeId = typeId;
		if (person != null)
			this.person = new PersonECEVo(person, nameSelfDetermination);

	}
}
