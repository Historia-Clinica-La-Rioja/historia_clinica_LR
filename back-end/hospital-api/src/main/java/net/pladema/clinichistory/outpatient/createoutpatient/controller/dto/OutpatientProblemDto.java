package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import net.pladema.clinichistory.outpatient.createoutpatient.controller.constraints.ProblemDates;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Valid
@ProblemDates
public class OutpatientProblemDto {


    @Nullable
    private String statusId;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @Nullable
    private String verificationId;

    @Nullable
    private String severity;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @Nullable
    private String startDate;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    @Nullable
    private String endDate;

    private boolean chronic;
}
