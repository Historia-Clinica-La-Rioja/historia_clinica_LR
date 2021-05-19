package ar.lamansys.sgx.shared.exceptions.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ApiErrorDto {
    
    private String message;
    
    private List<String> errors;
 
    public ApiErrorDto(String message, List<String> errors) {
        super();
        this.message = message;
        this.errors = errors;
    }
 
    public ApiErrorDto(String message, String error) {
        super();
        this.message = message;
        errors = Arrays.asList(error);
    }
}