package net.pladema.medicalconsultation.diary.repository.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.repository.entity.Diary;

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

    private final boolean automaticRenewal;

    private final boolean professionalAssignShift;

    private final boolean includeHoliday;

	private Integer clinicalSpecialtyId;

    public DiaryListVo(Diary diary, String doctorsOfficeDescription) {
        this.id = diary.getId();
        this.doctorsOfficeId = diary.getDoctorsOfficeId();
        this.doctorsOfficeDescription = doctorsOfficeDescription;
        this.startDate = diary.getStartDate();
        this.endDate = diary.getEndDate();
        this.appointmentDuration = diary.getAppointmentDuration();
        this.automaticRenewal = diary.isAutomaticRenewal();
        this.professionalAssignShift = diary.isProfessionalAsignShift();
        this.includeHoliday = diary.isIncludeHoliday();
		this.clinicalSpecialtyId = diary.getClinicalSpecialtyId();
    }

}
