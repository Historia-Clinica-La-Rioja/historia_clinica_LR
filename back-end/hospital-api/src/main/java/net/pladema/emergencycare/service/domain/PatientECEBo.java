package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.emergencycare.repository.domain.PatientECEVo;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientECEBo {

	private Integer id;

	private Integer patientMedicalCoverageId;

	private Short typeId;

	private PersonECEBo person;

	public PatientECEBo(PatientECEVo patient){
		this.id = patient.getId();
		this.patientMedicalCoverageId = patient.getPatientMedicalCoverageId();
		this.typeId = patient.getTypeId();
		this.person = new PersonECEBo(patient.getPerson());
	}

	public PatientECEBo(Integer patientId, Integer patientMedicalCoverageId) {
		this.id = patientId;
		this.patientMedicalCoverageId = patientMedicalCoverageId;
	}
}
