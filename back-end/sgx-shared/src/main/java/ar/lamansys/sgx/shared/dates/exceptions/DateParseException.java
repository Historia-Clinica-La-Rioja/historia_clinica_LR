package ar.lamansys.sgx.shared.dates.exceptions;

public class DateParseException extends RuntimeException {
	public final String originalDateValue;
	public DateParseException(String originalDateValue, Exception e) {
		super(e.getMessage(), e);
		this.originalDateValue = originalDateValue;
	}
}
