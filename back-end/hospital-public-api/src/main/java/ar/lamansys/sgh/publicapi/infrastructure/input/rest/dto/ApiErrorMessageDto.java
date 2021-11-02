package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorMessageDto {

    private String code;
    private String text;
}
