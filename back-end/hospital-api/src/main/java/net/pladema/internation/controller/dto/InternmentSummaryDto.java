package net.pladema.internation.controller.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InternmentSummaryDto {

    private Integer id;

    private BedDto bed;

    private ResponsibleDoctorDto doctor;

    private LocalDateTime createdOn;

    private int totalInternmentDays;

    private ClinicalSpecialtyDto specialty;
}
