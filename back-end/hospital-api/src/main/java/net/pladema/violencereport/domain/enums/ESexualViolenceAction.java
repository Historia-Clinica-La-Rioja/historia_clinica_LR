package net.pladema.violencereport.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ESexualViolenceAction {

	STI_LABORATORY_PRESCRIPTION(1, "Indicación de estudios de laboratorio para determinar ITS"),
	HIV_STI_HEPATITIS_PROPHYLAXIS(2, "Profilaxis para VIH, ITS y hepatitis virales frente a la exposición de fluidos potencialmente infecciosos"),
	EMERGENCY_CONTRACEPTION_PRESCRIPTION(3, "Indicación de anticoncepción de emergencia"),
	LEGAL_INTERRUPTION_PREGNANCY_COUNSELING(4, "Consejería de acceso a Interrupción Legal de Embarazo (ILE) frente a confirmación de embarazo producto de violación");

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
