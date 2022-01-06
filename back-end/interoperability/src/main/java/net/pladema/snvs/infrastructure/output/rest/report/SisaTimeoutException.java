package net.pladema.snvs.infrastructure.output.rest.report;

import lombok.Getter;

@Getter
public class SisaTimeoutException extends RuntimeException {

	private SisaEnumException code;

	public SisaTimeoutException(SisaEnumException code, String message) {
		super(message);
		this.code = code;
	}
}

