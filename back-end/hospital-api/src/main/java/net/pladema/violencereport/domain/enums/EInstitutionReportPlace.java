package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EInstitutionReportPlace {

	POLICE_STATION(1, "Comisaría"),
	POLICE_STATION_WOMEN_OFFICE(2, "Comisaría/Oficina de la Mujer"),
	PUBLIC_PROSECUTORS_OFFICE(3, "Fiscalía"),
	FAMILY_COURT(4, "Juzgado de la Familia"),
	PEACE_COURT(5, "Juzgado de Paz"),
	DIGITAL_SECURITY_REPORT(6, "Denuncia medio digital de seguridad pasa a fiscalía"),
	OTHER(7, "Otro");

	private Short id;

	private String value;

	EInstitutionReportPlace(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EInstitutionReportPlace map(Short id) {
		for (EInstitutionReportPlace e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("institution-report-place-not-exists", String.format("La opción %s no existe", id));
	}


}
