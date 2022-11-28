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

    protected String doctorsOfficeDescription;

    protected LocalDate startDate;

    protected LocalDate endDate;

    protected Short appointmentDuration;

    protected boolean automaticRenewal = false;

    protected boolean professionalAssignShift = false;

    protected boolean includeHoliday = false;

    protected boolean active = true;

    protected List<DiaryOpeningHoursBo> diaryOpeningHours;
    
    protected boolean deleted = false;

	protected Integer clinicalSpecialtyId;

	protected List<Integer> diaryAssociatedProfessionalsId;

	protected String alias;

	protected String clinicalSpecialtyName;

	protected List<Integer> careLines;

	protected Integer protectedAppointmentsPercentage;
    
}
