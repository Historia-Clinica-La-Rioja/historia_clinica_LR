package net.pladema.edMonton.domain.exception;

public class EdMontonException extends RuntimeException{

	private final EdMontonEnumException code;

	public EdMontonException(EdMontonEnumException code, String message) {
		super(message);
		this.code = code;
	}
}
