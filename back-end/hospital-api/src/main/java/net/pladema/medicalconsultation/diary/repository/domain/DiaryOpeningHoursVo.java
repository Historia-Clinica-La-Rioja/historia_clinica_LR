package net.pladema.medicalconsultation.diary.repository.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

@Getter
@ToString
@Value
public class DiaryOpeningHoursVo {

    private final Integer diaryId;

    private final OpeningHours openingHours;

    private final Short medicalAttentionTypeId;

    private final Short overturnCount;

    private final Boolean externalAppointmentsAllowed;

	private final Boolean protectedAppointmentsAllowed;

	private final Boolean onSiteAttentionAllowed;

	private final Boolean patientVirtualAttentionAllowed;

	private final Boolean secondOpinionVirtualAttentionAllowed;

	private final Boolean regulationProtectedAppointmentsAllowed;

    public DiaryOpeningHoursVo(Integer diaryId, OpeningHours op, Short medicalAttentionTypeId,
							   Short overturnCount, Boolean externalAppointmentsAllowed){
        this.diaryId = diaryId;
        this.openingHours = op;
        this.medicalAttentionTypeId = medicalAttentionTypeId;
        this.overturnCount = overturnCount;
        this.externalAppointmentsAllowed = externalAppointmentsAllowed;
		this.protectedAppointmentsAllowed = null;
		this.onSiteAttentionAllowed = null;
		this.patientVirtualAttentionAllowed = null;
		this.secondOpinionVirtualAttentionAllowed = null;
		this.regulationProtectedAppointmentsAllowed = null;
    }

	public DiaryOpeningHoursVo(Integer diaryId, OpeningHours op, Short medicalAttentionTypeId,
							   Short overturnCount, Boolean externalAppointmentsAllowed,
							   Boolean protectedAppointmentsAllowed, Boolean onSiteAttentionAllowed,
							   Boolean patientVirtualAttentionAllowed, Boolean secondOpinionVirtualAttentionAllowed, Boolean regulationProtectedAppointmentsAllowed){
		this.diaryId = diaryId;
		this.openingHours = op;
		this.medicalAttentionTypeId = medicalAttentionTypeId;
		this.overturnCount = overturnCount;
		this.externalAppointmentsAllowed = externalAppointmentsAllowed;
		this.protectedAppointmentsAllowed = protectedAppointmentsAllowed;
		this.onSiteAttentionAllowed = onSiteAttentionAllowed;
		this.patientVirtualAttentionAllowed = patientVirtualAttentionAllowed;
		this.secondOpinionVirtualAttentionAllowed = secondOpinionVirtualAttentionAllowed;
		this.regulationProtectedAppointmentsAllowed = regulationProtectedAppointmentsAllowed;
	}

}
