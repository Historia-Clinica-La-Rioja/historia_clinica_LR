package net.pladema.medicalconsultation.diary.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.domain.IDiaryOpeningHoursBo;
import net.pladema.medicalconsultation.diary.domain.IOpeningHoursBo;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"diaryId", "overturnCount"})
@ToString
@NoArgsConstructor
public class DiaryOpeningHoursBo implements IDiaryOpeningHoursBo {

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

    @Override
    public IOpeningHoursBo getIOpeningHours() {
        return openingHours;
    }
}
