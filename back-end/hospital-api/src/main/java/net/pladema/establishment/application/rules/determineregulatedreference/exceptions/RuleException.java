package net.pladema.establishment.application.rules.determineregulatedreference.exceptions;

import lombok.Getter;

@Getter
public class RuleException extends RuntimeException {

	public final RuleExceptionEnum code;

	public RuleException(RuleExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
