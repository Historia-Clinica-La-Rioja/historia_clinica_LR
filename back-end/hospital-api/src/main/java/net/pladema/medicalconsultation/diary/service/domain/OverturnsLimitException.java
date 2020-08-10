package net.pladema.medicalconsultation.diary.service.domain;

public class OverturnsLimitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6088130681022560852L;

	@Override
	public String getMessage() {
		return "diary.overturns.limitExceeded";
	}
	
}
