package net.pladema.electronicjointsignature.rejectsignature.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum ERejectDocumentElectronicJointSignatureReason {

	WRONG_PROFESSIONAL((short) 1),
	OTHER((short) 2);

	private final Short id;

	ERejectDocumentElectronicJointSignatureReason(Short id) {
		this.id = id;
	}

	public static ERejectDocumentElectronicJointSignatureReason map(Short id) {
		for (ERejectDocumentElectronicJointSignatureReason e : values()) {
			if (e.id.equals(id)) return e;
		}
		throw new NotFoundException("reson-not-exists", String.format("La razón %s no existe", id));
	}

}
