package net.pladema.snowstorm.services.exceptions;

public class SnowstormStatusException extends RuntimeException {
	public SnowstormStatusException(Throwable cause) {
		super(cause.getMessage(), cause);
	}
}
