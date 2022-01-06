package ar.lamansys.sgh.shared.infrastructure.input.service.snowstorm.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class SnowstormPortException extends Exception {

    private SnowstormPortEnumException code;

    private HttpStatus statusCode;

    public SnowstormPortException(SnowstormPortEnumException snowstormPortEnumException, HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.code = snowstormPortEnumException;
    }
}