package net.pladema.emergencycare.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;

import net.pladema.emergencycare.application.getevolutionnotebydocumentid.exceptions.GetEvolutionNoteByDocumentIdException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = {"net.pladema.emergencycare"})
public class EmergencyCareExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({GetEvolutionNoteByDocumentIdException.class})
	protected ApiErrorMessageDto handleCreateInternmentEpisodeException(GetEvolutionNoteByDocumentIdException ex) {
		log.debug("CreateInternmentEpisodeException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

}
