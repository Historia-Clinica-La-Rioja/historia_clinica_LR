package net.pladema.medicalconsultation.diary.repository.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@ToString
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DiaryListVo {

    private final Integer id;

    private final Integer doctorsOfficeId;

    private final String doctorsOfficeDescription;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final Short appointmentDuration;

    private final Boolean professionalAssignShift;

    private final Boolean includeHoliday;

}
