package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EProvincialGovernmentDevice {

	WOMEN_GENDER_DIVERSITY_MINISTRY(1, "Ministerio de mujeres, políticas de género y deversidad sexual"),
	PROMOTION_PROTECTION_RIGHTS_ZONAL_SERVICE(2, "Servicio zonal de promoción y protección de derechos de NNyA"),
	COMMUNITY_DEVELOPMENT_MINISTRY(3, "Ministerio de desarrollo de la comunidad"),
	EDUCATIONAL_INSTITUTION(4, "Instituciones educativas"),
	SECURITY_FORCES(5, "Fuerzas de seguridad/comisarias"),
	JUDICIAL_SYSTEM(6, "Sistema judicial/juzgados de paz y de familia"),
	PAROLE_BOARD(7, "Patronato de liberados"),
	JUVENILE_JUSTICE_INSTITUTION(8, "Instituciones del sistema de responsabilidad penal juvenil"),
	JUSTICE_MINISTRY(9, "Ministerio de Justicia y Derechos Humanos"),;

	private Short id;

	private String value;

	EProvincialGovernmentDevice(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EProvincialGovernmentDevice map(Short id) {
		for (EProvincialGovernmentDevice e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("provincial-state-option-not-exists", String.format("La opción %s no existe", id));
	}


}
