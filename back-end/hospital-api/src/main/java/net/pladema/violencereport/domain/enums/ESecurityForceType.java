package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ESecurityForceType {

	EX_COMBATANT(1, "EX_COMBATANT"),
	ARMED_FORCES(2, "ARMED_FORCES"),
	FEDERAL_POLICE(3, "FEDERAL_POLICE"),
	PROVINCIAL_POLICE(4, "PROVINCIAL_POLICE"),
	PRIVATE_SECURITY(5, "PRIVATE_SECURITY"),
	PENITENTIARY_SERVICE(6, "PENITENTIARY_SERVICE"),
	OTHER(7, "OTHER"),
	NO_INFORMATION(8, "NO_INFORMATION"),;

	private Short id;

	private String value;

	ESecurityForceType(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ESecurityForceType map(Short id) {
		for (ESecurityForceType e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("security-force-type-not-exists", String.format("El tipo %s no existe", id));
	}

}
