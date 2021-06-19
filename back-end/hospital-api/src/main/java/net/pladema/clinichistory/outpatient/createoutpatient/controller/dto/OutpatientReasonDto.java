package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OutpatientReasonDto {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

}
