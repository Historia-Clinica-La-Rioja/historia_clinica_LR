package net.pladema.patient.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.entity.Person;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PatientRegistrationSearch {

	private Person person;

	private Integer patientId;

	private float ranking;

	private Short patientTypeId;

	private Boolean toAudit;

	private String nameSelfDetermination;

	public PatientRegistrationSearch(Person person, Integer patientId, Short patientTypeId, Boolean toAudit, String nameSelfDetermination) {
		this.person = person;
		this.patientId = patientId;
		this.ranking = 0.0f;
		this.patientTypeId = patientTypeId;
		this.toAudit = toAudit;
		this.nameSelfDetermination = nameSelfDetermination;
	}
}
