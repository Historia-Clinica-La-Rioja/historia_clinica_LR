package ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class PersonInfoDto implements Serializable {

    private String identificationNumber;
    private String firstName;
    private String lastName;
    private DateDto birthDate;
    private Short genderId;
}
