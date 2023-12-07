package net.pladema.template.infrastructure.input.rest.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.domain.exceptions.DocumentTemplateException;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.template")
@RequiredArgsConstructor
@Slf4j
public class DocumentTemplateExceptionHandler {

    private final MessageSource messageSource;

    private ApiErrorMessageDto buildErrorMessage(String error, Locale locale) {
        return new ApiErrorMessageDto(
                error,
                messageSource.getMessage(error, null, error, locale)
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DocumentTemplateException.class})
    protected ApiErrorMessageDto handleDocumentTemplateException(DocumentTemplateException ex, Locale locale) {
        log.debug("DocumentTemplateException exception -> {}", ex.getMessage());
        return buildErrorMessage(ex.getMessage(), locale);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({JsonProcessingException.class})
    protected ApiErrorMessageDto handleJsonProcessingException(JsonProcessingException ex, Locale locale) {
        log.debug("JsonProcessingException exception -> {}", ex.getMessage());
        return buildErrorMessage(ex.getMessage(), locale);
    }
}
