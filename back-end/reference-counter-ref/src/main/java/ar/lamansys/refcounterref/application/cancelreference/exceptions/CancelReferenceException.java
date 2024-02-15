package ar.lamansys.refcounterref.application.cancelreference.exceptions;

import lombok.Getter;

@Getter
public class CancelReferenceException extends RuntimeException{

	private final CancelReferenceExceptionEnum code;

	public CancelReferenceException(CancelReferenceExceptionEnum code, String message){
		super(message);
		this.code = code;
	}
}
