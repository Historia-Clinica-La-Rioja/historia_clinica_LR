package net.pladema.clinichistory.hospitalization.controller.dto;


import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.DocumentsSummaryDto;
import net.pladema.establishment.controller.dto.BedDto;

import javax.annotation.Nullable;
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

    @Nullable
    private ResponsibleContactDto responsibleContact;
}
