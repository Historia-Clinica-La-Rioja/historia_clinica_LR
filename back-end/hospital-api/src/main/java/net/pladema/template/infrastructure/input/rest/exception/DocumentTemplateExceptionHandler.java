package net.pladema.template.infrastructure.input.rest.exception;

import ar.lamansys.sgx.shared.exceptions.dto.ApiErrorMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import net.pladema.template.domain.exceptions.DocumentTemplateException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "net.pladema.template")
@Slf4j
public class DocumentTemplateExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DocumentTemplateException.class})
    protected ApiErrorMessageDto handleDocumentTemplateException(DocumentTemplateException ex) {
        log.debug("DuplicateTemplateNameException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto(ex.getCode().toString(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({JsonProcessingException.class})
    protected ApiErrorMessageDto handleJsonProcessingException(JsonProcessingException ex) {
        log.debug("JsonProcessingException exception -> {}", ex.getMessage());
        return new ApiErrorMessageDto("document.template.error.processing", ex.getMessage());
    }
}
