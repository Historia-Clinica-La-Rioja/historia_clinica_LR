package ar.lamansys.sgx.shared.dates.exceptions;

public class DateException extends RuntimeException {
	public final String messageId;

	public DateException(String messageId, String message) {
		super(message);
		this.messageId = messageId;
	}
}