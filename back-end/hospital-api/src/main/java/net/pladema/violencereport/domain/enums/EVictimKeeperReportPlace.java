package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EVictimKeeperReportPlace {

	POLICE_STATION(1, "Comisaría"),
	POLICE_STATION_WOMEN_OFFICE(2, "Comisaría/Oficina de la Mujer"),
	PUBLIC_PROSECUTORS_OFFICE(3, "Fiscalía"),
	FAMILY_COURT(4, "Juzgado de la Familia"),
	PEACE_COURT(5, "Juzgado de Paz");

	private Short id;

	private String value;

	EVictimKeeperReportPlace(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EVictimKeeperReportPlace map(Short id) {
		for (EVictimKeeperReportPlace e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("victim-keeper-report-place-not-exists", String.format("La opción %s no existe", id));
	}

}
