package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ESexualViolenceAction {

	STI_LABORATORY_PRESCRIPTION(1, "STI_LABORATORY_PRESCRIPTION"),
	HIV_STI_HEPATITIS_PROPHYLAXIS(2, "HIV_STI_HEPATITIS_PROPHYLAXIS"),
	EMERGENCY_CONTRACEPTION_PRESCRIPTION(3, "EMERGENCY_CONTRACEPTION_PRESCRIPTION"),
	LEGAL_INTERRUPTION_PREGNANCY_COUNSELING(4, "LEGAL_INTERRUPTION_PREGNANCY_COUNSELING");

	private Short id;

	private String value;

	ESexualViolenceAction(Integer id, String value) {
		this.id = id.shortValue();
		this.value = value;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ESexualViolenceAction map(Short id) {
		for (ESexualViolenceAction e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("sexual-violence-action-not-exists", String.format("La opción %s no existe", id));
	}

}
