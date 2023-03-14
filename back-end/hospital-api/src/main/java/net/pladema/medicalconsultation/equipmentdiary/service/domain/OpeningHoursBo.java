package net.pladema.medicalconsultation.equipmentdiary.service.domain;

import lombok.*;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true, exclude = "id")
@NoArgsConstructor
public class OpeningHoursBo extends TimeRangeBo {

    private Integer id;

    private Short dayWeekId;

    public OpeningHoursBo(OpeningHours openingHours){
        super(openingHours.getFrom(), openingHours.getTo());
        this.dayWeekId = openingHours.getDayWeekId();
        this.id = openingHours.getId();
    }

    public boolean overlap(OpeningHoursBo other){
        return dayWeekId.equals(other.getDayWeekId()) &&
                getFrom().compareTo(other.getTo()) < 0 &&
                getTo().compareTo(other.getFrom()) > 0;

    }
}
