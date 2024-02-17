package ar.lamansys.sgh.publicapi.digitalsignature.application.port.out.exception;

import lombok.Getter;

@Getter
public class DigitalSignatureCallbackException extends Exception {

	private DigitalSignatureCallbackEnumException code;

	public DigitalSignatureCallbackException(DigitalSignatureCallbackEnumException code, String message) {
		super(message);
		this.code = code;
	}
}