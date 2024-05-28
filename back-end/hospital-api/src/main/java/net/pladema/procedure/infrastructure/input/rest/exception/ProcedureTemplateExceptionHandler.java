package net.pladema.procedure.infrastructure.input.rest.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.procedure.application.exceptions.ProcedureTemplateNotFoundException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.procedure")
public class ProcedureTemplateExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({ProcedureTemplateNotFoundException.class})
	protected ApiErrorMessageDto handleActivityNotFoundException(ProcedureTemplateNotFoundException ex) {
		log.debug("ProcedureTemplateNotFoundException -> {}", ex.toString());
		return new ApiErrorMessageDto("procedure-template-not-found", String.format("El estudio %s no existe", ex.getProcedureTemplateId()));
	}
}
