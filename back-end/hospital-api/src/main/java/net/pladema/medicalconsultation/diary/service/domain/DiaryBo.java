package net.pladema.medicalconsultation.diary.service.domain;

import ar.lamansys.sgx.shared.exceptions.SelfValidating;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.appointment.domain.UpdateDiaryAppointmentBo;
import net.pladema.medicalconsultation.diary.service.exception.DiaryEnumException;
import net.pladema.medicalconsultation.diary.service.exception.DiaryException;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class DiaryBo extends SelfValidating<DiaryBo> {

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

	protected DiaryBookingRestrictionBo bookingRestriction;
    
	public DiaryBo(LocalDate endDate, Short appointmentDuration) {
		this.endDate = endDate;
		this.appointmentDuration = appointmentDuration;
	}

	public DiaryBo(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public void validateSelf() {
		super.validateSelf();
		if (this.getPredecessorProfessionalId() != null && this.getHierarchicalUnitId() == null)
			throw new DiaryException(DiaryEnumException.PREDECESSOR_PROFESSIONAL_WITHOUT_HIERARCHICAL_UNIT,
					"No se puede ingresar un profesional a reemplazar sin seleccionar la unidad jerárquica a la que pertenece");
		this.getDiaryOpeningHours().forEach(openingHour -> {
			if (openingHour.getOnSiteAttentionAllowed() == null && openingHour.getPatientVirtualAttentionAllowed() == null && openingHour.getSecondOpinionVirtualAttentionAllowed() == null)
				throw new DiaryException(DiaryEnumException.MODALITY_NOT_FOUND, "Una de las franjas horarias no cuenta con una modalidad definida");
		});
	}

	protected boolean isOutOfDiaryBounds(UpdateDiaryAppointmentBo a) {
		LocalDate from = this.getStartDate();
		LocalDate to = this.getEndDate();
		return a.getDate().isBefore(from) || a.getDate().isAfter(to);
	}

	public void updateMyDiaryOpeningHours() {
		this.getDiaryOpeningHours()
				.forEach(doh -> doh.updateMeWithDiaryInformation(this));
	}

	public boolean equalsDoctorsOffice(DiaryBo other) {
		return this.getDoctorsOfficeId().equals(other.getDoctorsOfficeId());
	}
}
