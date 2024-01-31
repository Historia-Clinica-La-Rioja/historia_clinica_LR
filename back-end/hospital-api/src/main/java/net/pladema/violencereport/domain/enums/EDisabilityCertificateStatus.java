package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EDisabilityCertificateStatus {

	HAS_CERTIFICATE(1, "Si"),
	HAS_NOT_CERTIFICATE(2, "No"),
	PENDING(3, "En trámite"),
	NO_INFORMATION(4, "Sin información");

	private Short id;

	private String value;

	EDisabilityCertificateStatus(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static EDisabilityCertificateStatus map(Short id) {
		for (EDisabilityCertificateStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("disability-certificate-status-not-exists", String.format("El estado %s no existe", id));
	}
}
