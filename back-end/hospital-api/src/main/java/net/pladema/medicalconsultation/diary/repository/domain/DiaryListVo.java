package net.pladema.medicalconsultation.diary.repository.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;


@ToString
@AllArgsConstructor
@Value
public class DiaryListVo {

    private final Integer id;

    private final Integer doctorsOfficeId;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final Short appointmentDuration;

    private final Boolean professionalAssignShift;

    private final Boolean includeHoliday;

}
