package ar.lamansys.refcounterref.application.updatereferenceregulationstate.exceptions;

import lombok.Getter;

@Getter
public class UpdateReferenceRegulationStateException extends RuntimeException{

	public final UpdateReferenceRegulationStateExceptionEnum code;

	public UpdateReferenceRegulationStateException(UpdateReferenceRegulationStateExceptionEnum code, String message){
		super(message);
		this.code = code;
	}

}
