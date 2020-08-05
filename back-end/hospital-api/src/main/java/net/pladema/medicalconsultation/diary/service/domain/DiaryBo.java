package net.pladema.medicalconsultation.diary.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
public class DiaryBo {

    protected Integer id;

    protected Integer healthcareProfessionalId;

    protected Integer doctorsOfficeId;

    protected LocalDate startDate;

    protected LocalDate endDate;

    protected Short appointmentDuration;

    protected Boolean automaticRenewal = false;

    protected Boolean professionalAsignShift = false;

    protected Boolean includeHoliday = false;

    protected List<DiaryOpeningHoursBo> diaryOpeningHours;
}
