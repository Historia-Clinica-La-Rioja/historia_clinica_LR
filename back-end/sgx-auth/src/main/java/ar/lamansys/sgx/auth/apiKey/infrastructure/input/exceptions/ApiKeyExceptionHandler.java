package ar.lamansys.sgx.auth.apiKey.infrastructure.input.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "ar.lamansys.sgx.auth.apiKey")
public class ApiKeyExceptionHandler {


}

