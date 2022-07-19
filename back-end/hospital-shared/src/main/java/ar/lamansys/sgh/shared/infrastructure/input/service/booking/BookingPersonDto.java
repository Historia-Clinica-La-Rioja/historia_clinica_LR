package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import lombok.*;

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
