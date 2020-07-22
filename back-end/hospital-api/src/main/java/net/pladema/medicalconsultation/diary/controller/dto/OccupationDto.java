package net.pladema.medicalconsultation.diary.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OccupationDto {

    //weekDayId
    private Short id;

    private String description;

    private List<TimeRangeDto> timeRanges = new ArrayList<>();
}
