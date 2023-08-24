package ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgh.publicapi.application.deleteexternalencounter.exceptions.DeleteExternalEncounterException;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ActivityStorageException;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ExternalClinicalHistoryStorageException;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ExternalEncounterStorageException;
import ar.lamansys.sgh.publicapi.application.saveexternalencounter.exceptions.SaveExternalEncounterException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientBoException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientExtendedBoException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updateresult.exceptions.UpdateResultException;
import ar.lamansys.sgh.publicapi.imagecenter.application.updatesize.exceptions.UpdateSizeException;
import ar.lamansys.sgh.publicapi.imagenetwork.application.check.exceptions.BadStudyTokenException;
import ar.lamansys.sgx.shared.auth.user.SecurityContextUtils;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgh.publicapi")
public class HospitalPublicApiExceptionHandler {

    private final Logger logger;

    public HospitalPublicApiExceptionHandler() {
        logger = LoggerFactory.getLogger(HospitalPublicApiExceptionHandler.class);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ ActivityStorageException.class })
    protected ApiErrorMessageDto handleImmunizationValidatorException(ActivityStorageException ex, Locale locale) {
        logger.debug("ActivityStorageException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

    @ExceptionHandler({ ExternalPatientBoException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiErrorMessageDto handleExternalPatientBoException(ExternalPatientBoException ex) {
        logger.error("ExternalPatientBoException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ExceptionHandler({ ExternalPatientExtendedBoException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiErrorMessageDto handleExternalPatienExtendedtBoException(ExternalPatientExtendedBoException ex) {
        logger.error("ExternalPatienExtendedtBoException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ExceptionHandler({ ExternalEncounterBoException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiErrorMessageDto handleExternalEncounterBoException(ExternalEncounterBoException ex) {
        logger.error("ExternalEncounterBoException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ExceptionHandler({ SaveExternalEncounterException.class })
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    protected ApiErrorMessageDto handleSaveExternalEncounterException(SaveExternalEncounterException ex) {
        logger.error("SaveExternalEncounterException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ExceptionHandler({ExternalEncounterStorageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiErrorMessageDto handleExternalEncounterStorageException(ExternalEncounterStorageException ex) {
        logger.error("ExternalEncounterStorageException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ExceptionHandler({ DeleteExternalEncounterException.class })
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    protected ApiErrorMessageDto handleDeleteExternalEncounterException(DeleteExternalEncounterException ex) {
        logger.error("DeleteExternalEncounterException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

	@ExceptionHandler({ExternalClinicalHistoryStorageException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	protected ApiErrorMessageDto handleExternalClinicalHistoryStorageException(ExternalClinicalHistoryStorageException ex) {
		logger.error("ExternalClinicalHistoryStorageException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MissingServletRequestParameterException.class })
	protected ApiErrorMessageDto handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		logger.debug("MissingServletRequestParameterException message -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(HttpStatus.BAD_REQUEST.toString(), "Faltan parámetros en la URL para completar la solicitud");
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ BadStudyTokenException.class })
	protected ApiErrorMessageDto handleBadStudyTokenException(BadStudyTokenException ex) {
		logger.debug("BadStudyTokenException message -> {}", ex.getMessage(), ex.getCause());
		return new ApiErrorMessageDto(
				"bad-token",
				ex.getMessage()
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ UpdateSizeException.class })
	protected ApiErrorMessageDto handleUpdateSizeException(UpdateSizeException ex) {
		logger.debug("UpdateSizeException message -> {}", ex.getMessage(), ex.getCause());
		return new ApiErrorMessageDto(
				"update-size-failed",
				ex.getMessage()
		);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ UpdateResultException.class })
	protected ApiErrorMessageDto handleUpdateResultException(UpdateResultException ex) {
		logger.debug("UpdateResultException message -> {}", ex.getMessage(), ex.getCause());
		return new ApiErrorMessageDto(
				"update-result-failed",
				ex.getMessage()
		);
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler({ PublicApiAccessDeniedException.class })
	protected ApiErrorMessageDto handlePublicApiForbiddenException(PublicApiAccessDeniedException ex) {
		var text = String.format("Access Denied to %s %s", ex.group, ex.endpoint);
		SecurityContextUtils.warn(text);
		logger.debug("PublicApiForbiddenException  -> {}", ex.getMessage(), ex);
		return new ApiErrorMessageDto(
				"access-denied",
				text
		);
	}

}
