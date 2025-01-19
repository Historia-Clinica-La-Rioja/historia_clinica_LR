package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EInstitutionReportReason {

	SERIOUS_EXTREMELY_INJURIES(1, "Lesiones graves o gravísimas a personas adultas"),
	AGAINST_CHILDHOOD_ADOLESCENCE(2, "Violencias contra niñeces y adolescencias"),
	OTHER(3, "Otro");

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
