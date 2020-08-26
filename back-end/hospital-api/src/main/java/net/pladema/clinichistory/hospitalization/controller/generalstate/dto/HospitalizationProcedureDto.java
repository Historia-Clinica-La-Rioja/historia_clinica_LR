package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class HospitalizationProcedureDto {

    @NotNull(message = "{value.mandatory}")
    @Valid
    private SnomedDto snomed;

}