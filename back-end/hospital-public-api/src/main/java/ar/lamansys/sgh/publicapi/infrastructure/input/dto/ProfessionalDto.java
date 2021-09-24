package ar.lamansys.sgh.publicapi.infrastructure.input.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class ProfessionalDto implements Serializable {

    Integer id;
    String firstName;
    String lastName;
    String licenceNumber;
    String indentificationNumber;
}
