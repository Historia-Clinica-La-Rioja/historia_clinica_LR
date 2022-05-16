package ar.lamansys.sgh.shared.infrastructure.input.service.booking;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class BookingDiaryDto {
    private final Integer id;
    private final Integer doctorsOfficeId;
    private final String doctorsOfficeDescription;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Short appointmentDuration;
    private final LocalTime from;
    private final LocalTime to;
    private final Integer openingHoursId;



}
