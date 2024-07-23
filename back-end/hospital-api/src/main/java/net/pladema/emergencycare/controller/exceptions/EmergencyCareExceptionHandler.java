package net.pladema.emergencycare.controller.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.emergencycare.application.getemergencycaredocumentheader.exceptions.GetEmergencyCareDocumentHeaderException;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeException;
import net.pladema.emergencycare.application.exception.EmergencyCareEpisodeStateException;
import net.pladema.emergencycare.application.getevolutionnotebydocumentid.exceptions.GetEvolutionNoteByDocumentIdException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.emergencycare")
public class EmergencyCareExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({GetEvolutionNoteByDocumentIdException.class})
	protected ApiErrorMessageDto handleGetEvolutionNoteByDocumentIdException(GetEvolutionNoteByDocumentIdException ex) {
		log.debug("GetEvolutionNoteByDocumentIdException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({SaveEmergencyCareEpisodeException.class})
	protected ApiErrorMessageDto handleCreateUpdateEmergencyCareException(SaveEmergencyCareEpisodeException ex) {
		log.debug("CreateUpdateEmergencyCareEpisodeException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({GetEmergencyCareDocumentHeaderException.class})
	protected ApiErrorMessageDto handleGetEmergencyCareDocumentHeaderException(GetEmergencyCareDocumentHeaderException ex) {
		log.debug("GetEmergencyCareDocumentHeaderException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler({EmergencyCareEpisodeException.class})
	protected ApiErrorMessageDto handleEmergencyCareEpisodeException(EmergencyCareEpisodeException ex) {
		log.debug("EmergencyCareEpisodeException exception -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)	
	@ExceptionHandler({EmergencyCareEpisodeStateException.class})
	protected ApiErrorMessageDto handleEmergencyCareStateException(EmergencyCareEpisodeStateException ex) {
		log.debug("EmergencyCareEpisodeStateException exception -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}
}
