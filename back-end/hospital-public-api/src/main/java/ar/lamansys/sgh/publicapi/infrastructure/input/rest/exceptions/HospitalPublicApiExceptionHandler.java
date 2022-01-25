package ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions;

import ar.lamansys.sgh.publicapi.application.deleteexternalencounter.exceptions.DeleteExternalEncounterException;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ActivityStorageException;
import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ExternalEncounterStorageException;
import ar.lamansys.sgh.publicapi.application.saveexternalencounter.exceptions.SaveExternalEncounterException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalEncounterBoException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientBoException;
import ar.lamansys.sgh.publicapi.domain.exceptions.ExternalPatientExtendedBoException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

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
        logger.error("ExternalEncounterBoException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ExceptionHandler({ExternalEncounterStorageException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiErrorMessageDto handleExternalEncounterStorageException(ExternalEncounterStorageException ex) {
        logger.error("ExternalEncounterBoException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }

    @ExceptionHandler({ DeleteExternalEncounterException.class })
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    protected ApiErrorMessageDto handleDeleteExternalEncounterException(DeleteExternalEncounterException ex) {
        logger.error("ExternalEncounterBoException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().name(), ex.getMessage());
    }
}
