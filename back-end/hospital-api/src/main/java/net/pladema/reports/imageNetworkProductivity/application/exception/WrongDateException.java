package net.pladema.reports.imageNetworkProductivity.application.exception;

import lombok.Getter;
import net.pladema.reports.imageNetworkProductivity.domain.exception.EWrongDateException;

@Getter
public class WrongDateException extends RuntimeException {

	public final EWrongDateException code;

	public WrongDateException(EWrongDateException code, String message) {
		super(message);
		this.code = code;
	}

}
