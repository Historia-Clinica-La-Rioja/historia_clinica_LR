package ar.lamansys.online.domain.professional;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Getter
public class ProfessionalAppointmentsBo {
    private final Integer doctorsOfficeId;
    private final Integer diaryId;
    private final Short candidateDay;
    private final Short appointmentDuration;
    private final LocalDate diaryStartDate;
    private final LocalDate diaryEndDate;
    private final LocalTime hourFrom;
    private final LocalTime hourTo;
    private final LocalTime appointmentHour;
    private final Short appointmentDay;
    private final LocalDate appointmentDate;
}
