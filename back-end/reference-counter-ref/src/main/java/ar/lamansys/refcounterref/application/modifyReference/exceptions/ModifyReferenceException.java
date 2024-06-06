package ar.lamansys.refcounterref.application.modifyReference.exceptions;

public class ModifyReferenceException extends RuntimeException {

	public final ModifyReferenceExceptionEnum code;

	public ModifyReferenceException(ModifyReferenceExceptionEnum code, String message){
		super(message);
		this.code = code;
	}
	
}
