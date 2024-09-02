package net.pladema.medicalconsultation.diary.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"diaryId", "overturnCount"})
@ToString
@NoArgsConstructor
public class DiaryOpeningHoursBo {

    private Integer diaryId;

    private OpeningHoursBo openingHours;

    private Short medicalAttentionTypeId;

    private Short overturnCount = 0;

    private Boolean externalAppointmentsAllowed;

	private Boolean protectedAppointmentsAllowed;

	private Boolean onSiteAttentionAllowed;

	private Boolean patientVirtualAttentionAllowed;

	private Boolean secondOpinionVirtualAttentionAllowed;

	private Boolean regulationProtectedAppointmentsAllowed;

	public boolean overlap(DiaryOpeningHoursBo current) {
		return openingHours.overlap(current.getOpeningHours());
	}

}
