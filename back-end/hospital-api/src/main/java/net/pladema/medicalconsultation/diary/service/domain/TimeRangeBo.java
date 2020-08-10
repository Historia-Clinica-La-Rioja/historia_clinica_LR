package net.pladema.medicalconsultation.diary.service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.repository.entity.OpeningHours;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class TimeRangeBo {

    private LocalTime from;

    private LocalTime to;

    public TimeRangeBo(OpeningHours openingHours){
        this.from = openingHours.getFrom();
        this.to = openingHours.getTo();
    }
}
