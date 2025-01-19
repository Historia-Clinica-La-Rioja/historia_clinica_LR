package net.pladema.electronicjointsignature.rejectsignature.application.exception;

import lombok.Getter;
import net.pladema.electronicjointsignature.rejectsignature.domain.exception.ERejectDocumentElectronicJointSignatureException;

@Getter
public class RejectDocumentElectronicJointSignatureException extends RuntimeException {

	private static final long serialVersionUID = 8571012256835979854L;

	private final ERejectDocumentElectronicJointSignatureException code;

	public RejectDocumentElectronicJointSignatureException(ERejectDocumentElectronicJointSignatureException code, String message) {
		super(message);
		this.code = code;
	}

}
