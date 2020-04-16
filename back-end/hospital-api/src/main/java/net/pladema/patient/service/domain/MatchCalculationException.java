package net.pladema.patient.service.domain;

public class MatchCalculationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8806544811642763431L;
	
	private static final String DEFAULT_MSG = "Error al calcular la coincidencia";

	public MatchCalculationException() {
		super(DEFAULT_MSG);
	}

	public MatchCalculationException(String message) {
		super(message);
	}

}
