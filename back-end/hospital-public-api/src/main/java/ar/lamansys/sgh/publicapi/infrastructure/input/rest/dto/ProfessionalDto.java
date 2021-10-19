package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class ProfessionalDto implements Serializable {

    private Integer id;
    private String firstName;
    private String lastName;
    private String licenceNumber;
    private String indentificationNumber;
}
