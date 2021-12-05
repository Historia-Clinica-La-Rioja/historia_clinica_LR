package ar.lamansys.nursing.infrastructure.input.rest.exception;

import ar.lamansys.nursing.application.exceptions.NursingConsultationException;
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
@RestControllerAdvice(basePackages = "ar.lamansys.nursing")
public class NursingExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(NursingExceptionHandler.class);

    public NursingExceptionHandler() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NursingConsultationException.class})
    protected ApiErrorMessageDto handleNursingConsultationException(NursingConsultationException ex, Locale locale) {
        LOG.debug("NursingConsultationException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

}
