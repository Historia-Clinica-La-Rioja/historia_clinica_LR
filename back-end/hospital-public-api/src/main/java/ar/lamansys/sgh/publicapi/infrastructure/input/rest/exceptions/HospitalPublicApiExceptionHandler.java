package ar.lamansys.sgh.publicapi.infrastructure.input.rest.exceptions;

import ar.lamansys.sgh.publicapi.application.port.out.exceptions.ActivityStorageException;
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

}
