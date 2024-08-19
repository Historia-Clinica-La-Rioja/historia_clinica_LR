package net.pladema.medicalconsultation.diary.service.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.domain.IDiaryBo;
import net.pladema.medicalconsultation.diary.domain.IDiaryOpeningHoursBo;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryBo implements IDiaryBo {

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

	protected Integer predecessorProfessionalId;

	protected Integer hierarchicalUnitId;

	protected List<Integer> practicesId;

	protected List<String> practices;

	protected Integer institutionId;

	protected Integer protectedAppointmentsPercentage;

	protected List<DiaryLabelBo> diaryLabelBo;
    
	public DiaryBo(LocalDate endDate, Short appointmentDuration) {
		this.endDate = endDate;
		this.appointmentDuration = appointmentDuration;
	}

	public DiaryBo(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public List<IDiaryOpeningHoursBo> getIDiaryOpeningHours() {
		return Collections.unmodifiableList(diaryOpeningHours);
	}

}
