package net.pladema.sgx.session.infrastructure.input.rest.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.authorization.application.port.exceptions.InvalidUserException;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.sgx.session")
public class SessionExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ InvalidUserException.class })
    protected ApiErrorMessageDto handleInvalidUserException(InvalidUserException ex) {
        log.debug("InvalidUserException -> {}", ex.getMessage(), ex);
        return new ApiErrorMessageDto(ex.code.toString(), ex.getMessage());
    }

}
