package net.pladema.medicalconsultation.diary.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.dates.repository.entity.EDayOfWeek;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OccupationBo {

    //weekDayId
    private Short id;

    private String description;

    private List<TimeRangeBo> timeRanges = new ArrayList<>();

    public void setDayWeek(EDayOfWeek day){
        this.id = day.getId();
        this.description = day.getDescription();
    }
}
