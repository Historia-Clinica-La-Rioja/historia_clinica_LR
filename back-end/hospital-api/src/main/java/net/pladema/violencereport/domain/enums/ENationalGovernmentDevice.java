package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ENationalGovernmentDevice {

	WOMEN_GENDER_DIVERSITY_MINISTRY(1, "Ministerio de Mujeres, Géneros y Diversidad"),
	CHILDHOOD_ADOLESCENCE_FAMILY_MINISTRY(2, "Secretaría de Niñez, Adolescencia y Familia"),
	SOCIAL_DEVELOPMENT_MINISTRY(3, "Ministerio de Desarrollo Social"),
	SEDRONAR(4, "SEDRONAR"),
	ANSES(5, "ANSES"),
	CIVIL_REGISTRY(6, "Registro de las personas"),
	EDUCATIONAL_INSTITUTION(7, "Instituciones educativas"),
	SECURITY_FORCES(8, "Fuerzas de seguridad"),
	JUSTICE_MINISTRY(9, "Ministerio de Justicia y Derechos Humanos"),;

	private Short id;

	private String value;

	ENationalGovernmentDevice(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ENationalGovernmentDevice map(Short id) {
		for (ENationalGovernmentDevice e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("national-state-option-not-exists", String.format("La opción %s no existe", id));
	}


}
