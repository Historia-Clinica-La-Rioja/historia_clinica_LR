package ar.lamansys.online.infraestructure.input.rest.booking.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookingPersonDto {
    private String birthDate;
    private String email;
    private String firstName;
    private Short genderId;
    private String idNumber;
    private String lastName;
}
