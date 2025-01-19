package ar.lamansys.odontology.infrastructure.controller.consultation.dto;

import java.io.Serializable;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.servicerequest.dto.CreateOutpatientServiceRequestDto;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OdontologyProcedureDto implements Serializable {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

    @Nullable
    @JsonFormat(pattern = JacksonDateFormatConfig.DATE_FORMAT)
    private String performedDate;

}
