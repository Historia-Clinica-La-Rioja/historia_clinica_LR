package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OutpatientAllergyConditionDto {


    @Nullable
    private String statusId;

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    private String categoryId;

    @NotNull(message = "{value.mandatory}")
    private Short criticalityId;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String startDate;

    @Nullable
    private String verificationId;
}
