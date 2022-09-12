package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class PublicAppointmentPersonDto {

    private String firstName;

    private String lastName;

    private String identificationNumber;

    private Short genderId;

}
