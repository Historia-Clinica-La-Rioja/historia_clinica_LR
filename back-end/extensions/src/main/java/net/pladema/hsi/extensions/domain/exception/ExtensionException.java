package net.pladema.hsi.extensions.domain.exception;

import lombok.Getter;

@Getter
public class ExtensionException extends RuntimeException {
	public final ExtensionExceptionEnum code;

	public ExtensionException(ExtensionExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}
}
