package net.pladema.hl7.dataexchange.exceptions;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public class ServiceRequestException extends RuntimeException{

	private ServiceRequestExceptionEnum code;
	private HttpStatus status;

	public ServiceRequestException(ServiceRequestExceptionEnum code, HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.code = code;
	}

	public String getCode() {
		return code.name();
	}

}
