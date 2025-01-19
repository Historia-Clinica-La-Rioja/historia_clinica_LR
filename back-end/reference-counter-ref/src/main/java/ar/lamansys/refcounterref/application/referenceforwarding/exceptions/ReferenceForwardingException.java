package ar.lamansys.refcounterref.application.referenceforwarding.exceptions;

import lombok.Getter;

@Getter
public class ReferenceForwardingException extends RuntimeException {

	private final ReferenceForwardingExceptionEnum code;

	public ReferenceForwardingException(ReferenceForwardingExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
