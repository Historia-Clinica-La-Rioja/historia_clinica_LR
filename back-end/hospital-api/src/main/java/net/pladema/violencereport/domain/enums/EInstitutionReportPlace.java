package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EInstitutionReportPlace {

	POLICE_STATION(1, "POLICE_STATION"),
	POLICE_STATION_WOMEN_OFFICE(2, "POLICE_STATION_WOMEN_OFFICE"),
	PUBLIC_PROSECUTORS_OFFICE(3, "PUBLIC_PROSECUTORS_OFFICE"),
	FAMILY_COURT(4, "FAMILY_COURT"),
	PEACE_COURT(5, "PEACE_COURT"),
	DIGITAL_SECURITY_REPORT(6, "DIGITAL_SECURITY_REPORT"),
	OTHER(7, "OTHER");

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
