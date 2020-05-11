package net.pladema.internation.controller.internment.dto;


import lombok.Getter;
import lombok.Setter;
import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.internation.controller.internment.dto.summary.DocumentsSummaryDto;

import java.time.LocalDate;

@Getter
@Setter
public class InternmentSummaryDto {

    private Integer id;

    private DocumentsSummaryDto documents;

    private BedDto bed;

    private ResponsibleDoctorDto doctor;

    private LocalDate entryDate;

    private int totalInternmentDays;

    private ClinicalSpecialtyDto specialty;
}
