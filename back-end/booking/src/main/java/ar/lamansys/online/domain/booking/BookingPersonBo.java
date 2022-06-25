package ar.lamansys.online.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookingPersonBo {
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String idNumber;
    private final Short genderId;
    private final String birthDate;
}
