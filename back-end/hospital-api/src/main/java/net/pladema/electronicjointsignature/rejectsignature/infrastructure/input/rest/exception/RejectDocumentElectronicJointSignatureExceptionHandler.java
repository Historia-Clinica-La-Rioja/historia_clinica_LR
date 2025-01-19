package net.pladema.electronicjointsignature.rejectsignature.infrastructure.input.rest.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import net.pladema.electronicjointsignature.rejectsignature.application.exception.RejectDocumentElectronicJointSignatureException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.electronicjointsignature.rejectsignature")
public class RejectDocumentElectronicJointSignatureExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({RejectDocumentElectronicJointSignatureException.class})
	protected ApiErrorMessageDto handleWrongDateException(RejectDocumentElectronicJointSignatureException ex) {
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

}
