package net.pladema.federar.services.exceptions;

public class SnowstormStatusException extends RuntimeException {
	public SnowstormStatusException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
