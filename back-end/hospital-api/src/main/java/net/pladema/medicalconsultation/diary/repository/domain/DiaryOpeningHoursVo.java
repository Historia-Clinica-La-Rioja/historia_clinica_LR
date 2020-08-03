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

    public DiaryOpeningHoursVo(Integer diaryId, OpeningHours op, Short medicalAttentionTypeId, Short overturnCount){
        this.diaryId = diaryId;
        this.openingHours = op;
        this.medicalAttentionTypeId = medicalAttentionTypeId;
        this.overturnCount = overturnCount;
    }
}
