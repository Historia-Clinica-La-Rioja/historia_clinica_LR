package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.sgx.dates.configuration.JacksonDateFormatConfig;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class HospitalizationProcedureDto {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @Nullable
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String fecha;
}