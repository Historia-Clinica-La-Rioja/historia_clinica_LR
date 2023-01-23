package ar.lamansys.sgh.publicapi.domain.exceptions;

import lombok.Getter;

@Getter
public class PrescriptionBoException extends Exception{
	private PrescriptionBoEnumException code;

	public PrescriptionBoException(PrescriptionBoEnumException code, String message) {
		super(message);
		this.code = code;
	}
}
