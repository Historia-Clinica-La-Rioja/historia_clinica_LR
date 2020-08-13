package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OpeningHoursDto extends TimeRangeDto {

    @Nullable
    private Integer id;

    @NotNull
    @Min(value = 0)
    @Max(value = 6)
    private Short dayWeekId;

    public boolean overlap(OpeningHoursDto other){
        return dayWeekId.equals(other.getDayWeekId()) &&
                getFrom().compareTo(other.getTo()) < 0 &&
                getTo().compareTo(other.getFrom()) > 0;

    }
}
