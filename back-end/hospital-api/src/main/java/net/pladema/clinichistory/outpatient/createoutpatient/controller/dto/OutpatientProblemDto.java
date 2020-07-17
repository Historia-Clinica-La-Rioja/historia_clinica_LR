package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OutpatientProblemDto {


    @Nullable
    private String statusId;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @Nullable
    private String verificationId;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @NotNull
    private String startDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @Nullable
    private String endDate;

    private boolean chronic;
}
