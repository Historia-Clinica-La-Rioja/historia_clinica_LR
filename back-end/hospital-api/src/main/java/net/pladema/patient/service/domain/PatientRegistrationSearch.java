package net.pladema.patient.service.domain;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;
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

	private EAuditType auditType;

	private String nameSelfDetermination;

	public PatientRegistrationSearch(Person person, Integer patientId, Short patientTypeId, Short auditTypeId, String nameSelfDetermination) {
		this.person = person;
		this.patientId = patientId;
		this.ranking = 0.0f;
		this.patientTypeId = patientTypeId;
		this.auditType = EAuditType.getById(auditTypeId);
		this.nameSelfDetermination = nameSelfDetermination;
	}
}
