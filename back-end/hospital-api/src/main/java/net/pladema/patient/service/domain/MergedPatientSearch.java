package net.pladema.patient.service.domain;

import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.person.repository.entity.Person;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class MergedPatientSearch {

	private Person person;

	private Integer patientId;

	private Short patientTypeId;

	private EAuditType auditType;

	private String nameSelfDetermination;

	private Integer numberOfMergedPatients;

	public MergedPatientSearch(Person person, Integer patientId, Short patientTypeId, Short auditTypeId, String nameSelfDetermination, BigInteger numberOfMergedPatients) {
		this.person = person;
		this.patientId = patientId;
		this.patientTypeId = patientTypeId;
		this.auditType = EAuditType.getById(auditTypeId);
		this.nameSelfDetermination = nameSelfDetermination;
		this.numberOfMergedPatients = numberOfMergedPatients.intValue();
	}
}
