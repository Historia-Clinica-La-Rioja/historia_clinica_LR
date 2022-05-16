package ar.lamansys.sgh.shared.infrastructure.input.service.appointment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PublicAppointmentPersonDto {

    private String firstName;

    private String lastName;

    private String identificationNumber;

    private Short genderId;

}
