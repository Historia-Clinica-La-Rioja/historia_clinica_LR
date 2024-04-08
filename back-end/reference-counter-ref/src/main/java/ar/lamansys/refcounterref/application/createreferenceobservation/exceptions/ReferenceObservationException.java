package ar.lamansys.refcounterref.application.createreferenceobservation.exceptions;

import lombok.Getter;

@Getter
public class ReferenceObservationException extends RuntimeException {

	private final ReferenceObservationExceptionEnum code;

	public ReferenceObservationException(ReferenceObservationExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
