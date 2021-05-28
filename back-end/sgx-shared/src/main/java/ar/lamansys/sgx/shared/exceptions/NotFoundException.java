package ar.lamansys.sgx.shared.exceptions;

public class NotFoundException extends RuntimeException {
	public final String messageId;

	public NotFoundException(String messageId, String message) {
		super(message);
		this.messageId = messageId;
	}
}
