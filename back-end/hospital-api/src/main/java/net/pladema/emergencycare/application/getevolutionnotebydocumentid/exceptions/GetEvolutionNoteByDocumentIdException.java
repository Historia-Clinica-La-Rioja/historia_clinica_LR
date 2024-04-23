package net.pladema.emergencycare.application.getevolutionnotebydocumentid.exceptions;

import lombok.Getter;

@Getter
public class GetEvolutionNoteByDocumentIdException extends RuntimeException {

	private GetEvolutionNoteByDocumentIdExceptionEnum code;

	public GetEvolutionNoteByDocumentIdException (GetEvolutionNoteByDocumentIdExceptionEnum code, String message){
		super(message);
		this.code = code;
	}
}
