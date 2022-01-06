package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class OutpatientReferenceProblemDto {

    @Nullable
    private String id;

    @Valid
    @NotNull(message = "{value.mandatory}")
    private SnomedDto snomed;

}
