package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OutpatientPersonalHistoryDto {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @NotNull
    private String startDate;

    @NotNull
    private Short typeId;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @Nullable
    private String inactivationDate;

    @Nullable
    private String note;
}
