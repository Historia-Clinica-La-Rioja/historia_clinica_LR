package net.pladema.sgx.exceptions;

public class NotFoundException extends RuntimeException {
	public final String messageId;

	public NotFoundException(String messageId, String message) {
		super(message);
		this.messageId = messageId;
	}
}
