package ar.lamansys.refcounterref.application.updatereferenceadministrativestate.exceptions;

import ar.lamansys.refcounterref.application.updatereferenceregulationstate.exceptions.UpdateReferenceRegulationStateExceptionEnum;
import lombok.Getter;

@Getter
public class UpdateReferenceAdministrativeStateException extends RuntimeException{

	private final UpdateReferenceAdministrativeStateExceptionEnum code;

	public UpdateReferenceAdministrativeStateException(UpdateReferenceAdministrativeStateExceptionEnum code, String message){
		super(message);
		this.code = code;
	}

}
