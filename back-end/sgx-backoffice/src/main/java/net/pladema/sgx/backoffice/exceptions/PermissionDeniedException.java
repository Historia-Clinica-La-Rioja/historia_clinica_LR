package net.pladema.sgx.backoffice.exceptions;

public class PermissionDeniedException extends RuntimeException {

	private static final long serialVersionUID = 3084453430287397853L;

	public PermissionDeniedException(String message) {
		super(message);
	}

}
