package net.pladema.medicalconsultation.diary.repository.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
public class OccupationVo {

    private Integer diaryId;

    private LocalDate startDate;

    private LocalDate endDate;

    private OpeningHours openingHours;

    public OccupationVo(Integer diaryId, LocalDate startDate, LocalDate endDate,
                        Short dayWeekId, LocalTime from, LocalTime to){
        this.diaryId = diaryId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.openingHours = new OpeningHours(dayWeekId, from, to);
    }
}
