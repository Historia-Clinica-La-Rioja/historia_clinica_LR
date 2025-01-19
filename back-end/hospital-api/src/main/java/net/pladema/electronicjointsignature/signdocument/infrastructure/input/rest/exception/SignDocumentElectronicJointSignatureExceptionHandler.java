package net.pladema.electronicjointsignature.signdocument.infrastructure.input.rest.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;

import net.pladema.electronicjointsignature.signdocument.application.exception.SignDocumentElectronicJointSignatureException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.electronicjointsignature.signdocument")
public class SignDocumentElectronicJointSignatureExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({SignDocumentElectronicJointSignatureException.class})
	protected ApiErrorMessageDto handleWrongDateException(SignDocumentElectronicJointSignatureException ex) {
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

}
