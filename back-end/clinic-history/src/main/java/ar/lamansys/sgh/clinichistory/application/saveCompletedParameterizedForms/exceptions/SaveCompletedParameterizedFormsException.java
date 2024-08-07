package ar.lamansys.sgh.clinichistory.application.saveCompletedParameterizedForms.exceptions;

import lombok.Getter;

@Getter
public class SaveCompletedParameterizedFormsException extends RuntimeException{

	private final SaveCompletedParameterizedFormsExceptionEnum code;

	public SaveCompletedParameterizedFormsException(SaveCompletedParameterizedFormsExceptionEnum code, String message){
		super(message);
		this.code = code;
	}

}
