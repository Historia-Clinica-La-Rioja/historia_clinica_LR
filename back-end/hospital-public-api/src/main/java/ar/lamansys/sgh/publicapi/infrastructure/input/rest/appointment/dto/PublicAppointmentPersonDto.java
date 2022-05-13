package ar.lamansys.sgh.publicapi.infrastructure.input.rest.appointment.dto;

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

    private Short identificationTypeId;

    private Short genderId;

    private String nameSelfDetermination;

	private String email;
}
