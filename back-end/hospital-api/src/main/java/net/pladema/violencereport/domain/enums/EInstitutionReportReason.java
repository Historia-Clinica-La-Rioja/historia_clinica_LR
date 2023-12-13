package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EInstitutionReportReason {

	SERIOUS_EXTREMELY_INJURIES(1, "SERIOUS_EXTREMELY_INJURIES"),
	AGAINST_CHILDHOOD_ADOLESCENCE(2, "AGAINST_CHILDHOOD_ADOLESCENCE"),
	OTHER(3, "OTHER");

	private Short id;

	private String value;

	EInstitutionReportReason(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EInstitutionReportReason map(Short id) {
		for (EInstitutionReportReason e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("institution-report-reason-not-exists", String.format("La opción %s no existe", id));
	}

}
