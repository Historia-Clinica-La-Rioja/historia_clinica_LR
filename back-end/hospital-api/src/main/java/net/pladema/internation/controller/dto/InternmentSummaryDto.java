package net.pladema.internation.controller.dto;


import lombok.Getter;
import lombok.Setter;
import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.internation.controller.dto.core.DocumentsSummaryDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class InternmentSummaryDto {

    private Integer id;

    private DocumentsSummaryDto documents;

    private BedDto bed;

    private ResponsibleDoctorDto doctor;

    private LocalDateTime createdOn;

    private int totalInternmentDays;

    private ClinicalSpecialtyDto specialty;
}
