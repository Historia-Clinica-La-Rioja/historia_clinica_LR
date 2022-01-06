package ar.lamansys.refcounterref.infraestructure.input.rest.exceptions;

import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = "ar.lamansys.refcounterref")
public class ReferenceExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ReferenceException.class})
    protected ApiErrorMessageDto handleReferenceException(ReferenceException ex, Locale locale) {
        log.debug("ReferenceException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

}
