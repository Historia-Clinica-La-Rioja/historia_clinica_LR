package ar.lamansys.sgh.clinichistory.domain.document.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EElectronicSignatureStatus {

	PENDING(1),
	REJECTED(2),
	SIGNED(3);

	private Short id;

	EElectronicSignatureStatus(Integer id) {
		this.id = id.shortValue();
	}

	public void setId(Short id) {
		this.id = id;
	}

	public static EElectronicSignatureStatus map(Short id) {
		for (EElectronicSignatureStatus e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("status-not-exists", String.format("El estado %s no existe", id));
	}

}
