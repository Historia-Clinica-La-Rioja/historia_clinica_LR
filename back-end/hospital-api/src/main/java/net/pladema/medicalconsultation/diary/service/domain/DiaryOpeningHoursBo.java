package net.pladema.medicalconsultation.diary.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DiaryOpeningHoursBo {

    private OpeningHoursBo openingHours;

    private Short medicalAttentionTypeId;

    private Short overturnCount = 0;
}
