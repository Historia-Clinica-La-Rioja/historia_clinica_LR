package net.pladema.medicalconsultation.diary.service.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OpeningHoursBo extends TimeRangeBo {

    private Integer id;

    private Short dayWeekId;

    public OpeningHoursBo(OpeningHours openingHours){
        super(openingHours.getFrom(), openingHours.getTo());
        this.dayWeekId = openingHours.getDayWeekId();
    }
}
