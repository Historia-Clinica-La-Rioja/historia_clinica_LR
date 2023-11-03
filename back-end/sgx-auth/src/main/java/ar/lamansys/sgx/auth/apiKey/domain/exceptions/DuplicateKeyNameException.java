package ar.lamansys.sgx.auth.apiKey.domain.exceptions;

public class DuplicateKeyNameException extends RuntimeException {
	public DuplicateKeyNameException(Exception e) {
		super(e);
	}
}
