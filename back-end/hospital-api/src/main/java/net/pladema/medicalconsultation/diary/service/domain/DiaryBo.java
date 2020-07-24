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

    private Integer id;

    private Integer healthcareProfessionalId;

    private Integer doctorsOfficeId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Short appointmentDuration;

    private Boolean automaticRenewal = false;

    private Boolean professionalAsignShift = false;

    private Boolean includeHoliday = false;

    private List<DiaryOpeningHoursBo> diaryOpeningHours;
}
