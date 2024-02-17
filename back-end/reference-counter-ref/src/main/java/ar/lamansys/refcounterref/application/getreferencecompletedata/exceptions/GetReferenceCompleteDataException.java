package ar.lamansys.refcounterref.application.getreferencecompletedata.exceptions;

import lombok.Getter;

@Getter
public class GetReferenceCompleteDataException extends RuntimeException {

	public final GetReferenceCompleteDataExceptionEnum code;

	public GetReferenceCompleteDataException(GetReferenceCompleteDataExceptionEnum code, String message) {
		super(message);
		this.code = code;
	}

}
