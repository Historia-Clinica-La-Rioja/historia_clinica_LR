package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TimeRangeBo {

    private LocalTime from;

    private LocalTime to;

    public TimeRangeBo(OpeningHours openingHours){
        this.from = openingHours.getFrom();
        this.to = openingHours.getTo();
    }
}
