package net.pladema.access.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EClinicHistoryAccessReason {

	MEDICAL_EMERGENCY(1, "Urgencia médica"),
	PROFESSIONAL_CONSULTATION(2, "Consulta profesional"),
	PATIENT_CONSULTATION(3, "Consulta de paciente"),
	AUDIT(4, "Auditoría");

	private Short id;
	private String value;

	EClinicHistoryAccessReason(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public static EClinicHistoryAccessReason map(Short id) {
		for(EClinicHistoryAccessReason e : values()) {
			if(e.id.equals(id)) return e;
		}
		throw new NotFoundException("clinic-history-access-reason-not-exists", String.format("El motivo %s no existe", id));
	}
}
