package ar.lamansys.sgx.shared.restclient.configuration.resttemplate.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class RestTemplateApiException extends Exception {

    private HttpStatus statusCode;

    public RestTemplateApiException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}