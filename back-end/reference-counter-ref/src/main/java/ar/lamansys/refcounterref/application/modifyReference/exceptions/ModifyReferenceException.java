package ar.lamansys.refcounterref.application.modifyReference.exceptions;

import lombok.Getter;

@Getter
public class ModifyReferenceException extends RuntimeException {

	public final ModifyReferenceExceptionEnum code;

	public ModifyReferenceException(ModifyReferenceExceptionEnum code, String message){
		super(message);
		this.code = code;
	}
	
}
