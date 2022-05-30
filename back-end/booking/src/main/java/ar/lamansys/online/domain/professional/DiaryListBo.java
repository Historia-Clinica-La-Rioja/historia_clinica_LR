package ar.lamansys.online.domain.professional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class DiaryListBo {
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
