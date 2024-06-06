package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ESecurityForceType {

	EX_COMBATANT(1, "Excombatient"),
	ARMED_FORCES(2, "Fuerzas armadas"),
	FEDERAL_POLICE(3, "Policía federal"),
	PROVINCIAL_POLICE(4, "Policía provincial"),
	PRIVATE_SECURITY(5, "Seguridad privada"),
	PENITENTIARY_SERVICE(6, "Servicio penitenciario"),
	OTHER(7, "Otras"),
	NO_INFORMATION(8, "Sin información"),;

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
