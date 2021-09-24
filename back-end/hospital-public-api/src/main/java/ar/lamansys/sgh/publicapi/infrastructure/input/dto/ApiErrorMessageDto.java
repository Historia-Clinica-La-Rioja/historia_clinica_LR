package ar.lamansys.sgh.publicapi.infrastructure.input.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorMessageDto {

    String code;
    String text;
}
