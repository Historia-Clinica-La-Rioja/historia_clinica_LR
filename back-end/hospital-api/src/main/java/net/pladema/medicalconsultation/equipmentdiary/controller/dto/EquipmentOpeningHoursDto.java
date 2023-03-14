package net.pladema.medicalconsultation.equipmentdiary.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.medicalconsultation.diary.controller.dto.TimeRangeDto;

import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class EquipmentOpeningHoursDto extends TimeRangeDto {

    @Nullable
    private Integer id;

    @NotNull
    @Min(value = 0)
    @Max(value = 6)
    private Short dayWeekId;

    public boolean overlap(EquipmentOpeningHoursDto other){
        return dayWeekId.equals(other.getDayWeekId()) &&
                getFrom().compareTo(other.getTo()) < 0 &&
                getTo().compareTo(other.getFrom()) > 0;

    }
}
