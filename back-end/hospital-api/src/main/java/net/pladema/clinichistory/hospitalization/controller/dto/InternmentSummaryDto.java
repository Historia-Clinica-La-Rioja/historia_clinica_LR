package net.pladema.clinichistory.hospitalization.controller.dto;


import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.dto.summary.DocumentsSummaryDto;
import net.pladema.establishment.controller.dto.BedDto;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
    private String probableDischargeDate;
}
