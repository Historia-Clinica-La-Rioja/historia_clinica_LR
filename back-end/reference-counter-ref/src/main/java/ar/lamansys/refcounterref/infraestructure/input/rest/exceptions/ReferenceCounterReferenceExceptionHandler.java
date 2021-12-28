package ar.lamansys.refcounterref.infraestructure.input.rest.exceptions;

import ar.lamansys.refcounterref.application.createcounterreference.exceptions.CreateCounterReferenceException;
import ar.lamansys.refcounterref.application.createcounterreferencefile.exceptions.CreateCounterReferenceFileException;
import ar.lamansys.refcounterref.application.createreferencefile.exceptions.CreateReferenceFileException;
import ar.lamansys.refcounterref.application.getcounterreferencefile.exceptions.GetCounterReferenceFileException;
import ar.lamansys.refcounterref.application.getreference.exceptions.ReferenceException;
import ar.lamansys.refcounterref.application.getreferencefile.exceptions.GetReferenceFileException;
import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorDto;
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
public class ReferenceCounterReferenceExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ReferenceException.class})
    protected ApiErrorMessageDto handleReferenceException(ReferenceException ex, Locale locale) {
        log.debug("ReferenceException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CreateCounterReferenceException.class})
    protected ApiErrorDto handleCreateCounterReferenceException(CreateCounterReferenceException ex, Locale locale) {
        log.debug("CreateCounterReferenceException exception -> {}", ex.getMessages());
        return new ApiErrorDto(ex.getCode().toString(), ex.getMessages());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CreateReferenceFileException.class})
    protected ApiErrorMessageDto handleCreateReferenceFileException(CreateReferenceFileException ex, Locale locale) {
        log.debug("CreateReferenceFileException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({GetReferenceFileException.class})
    protected ApiErrorMessageDto handleGetReferenceFileException(GetReferenceFileException ex, Locale locale) {
        log.debug("GetReferenceFileException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CreateCounterReferenceFileException.class})
    protected ApiErrorMessageDto handleCreateCounterReferenceFileException(CreateCounterReferenceFileException ex, Locale locale) {
        log.debug("CreateCounterReferenceFileException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({GetCounterReferenceFileException.class})
    protected ApiErrorMessageDto handleGetCounterReferenceFileException(GetCounterReferenceFileException ex, Locale locale) {
        log.debug("GetCounterReferenceFileException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }
}
