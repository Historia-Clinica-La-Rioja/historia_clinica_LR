package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Validated
public class OutpatientUpdateImmunizationDto {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String administrationDate;
}
