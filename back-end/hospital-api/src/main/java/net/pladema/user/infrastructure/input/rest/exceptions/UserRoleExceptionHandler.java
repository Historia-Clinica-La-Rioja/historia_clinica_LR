package net.pladema.user.infrastructure.input.rest.exceptions;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import net.pladema.user.application.port.exceptions.UserPersonStorageException;
import net.pladema.user.application.port.exceptions.UserRoleStorageException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.user")
@Slf4j
public class UserRoleExceptionHandler {

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler({UserRoleStorageException.class})
    protected ApiErrorMessageDto handleUserRoleStorageException(UserRoleStorageException ex) {
        log.debug("RegisterUserException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }


	@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
	@ExceptionHandler({UserPersonStorageException.class})
	protected ApiErrorMessageDto handleUserPersonStorageException(UserPersonStorageException ex) {
		log.debug("RegisterUserException exception -> {}", ex.getMessage());
		return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
	}
}



