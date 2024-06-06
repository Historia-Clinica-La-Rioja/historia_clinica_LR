package net.pladema.electronicjointsignature.signdocument.application.exception;

import lombok.Getter;
import net.pladema.electronicjointsignature.signdocument.domain.exception.ESignDocumentElectronicJointSignatureException;

@Getter
public class SignDocumentElectronicJointSignatureException extends RuntimeException {

	private static final long serialVersionUID = -4257952158489477567L;

	private final ESignDocumentElectronicJointSignatureException code;

	public SignDocumentElectronicJointSignatureException(String message, ESignDocumentElectronicJointSignatureException code) {
		super(message);
		this.code = code;
	}

}
