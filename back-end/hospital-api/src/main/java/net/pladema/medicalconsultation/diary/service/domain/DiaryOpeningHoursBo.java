package net.pladema.medicalconsultation.diary.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"diaryId", "overturnCount"})
@ToString
public class DiaryOpeningHoursBo {

    private Integer diaryId;

    private OpeningHoursBo openingHours;

    private Short medicalAttentionTypeId;

    private Short overturnCount = 0;

    private Boolean externalAppointmentsAllowed;

    public boolean overlap(DiaryOpeningHoursBo other) {
        return !(this.medicalAttentionTypeId.equals(other.getMedicalAttentionTypeId())) &&
                openingHours.overlap(other.getOpeningHours());

    }
}
