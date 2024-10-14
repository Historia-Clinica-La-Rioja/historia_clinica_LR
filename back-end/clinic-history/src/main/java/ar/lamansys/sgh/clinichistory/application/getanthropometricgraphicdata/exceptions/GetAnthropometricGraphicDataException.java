package ar.lamansys.sgh.clinichistory.application.getanthropometricgraphicdata.exceptions;

import lombok.Getter;

@Getter
public class GetAnthropometricGraphicDataException extends RuntimeException{

	private final GetAnthropometricGraphicDataExceptionEnum code;

	public GetAnthropometricGraphicDataException(GetAnthropometricGraphicDataExceptionEnum code, String message){
		super(message);
		this.code = code;
	}


}
