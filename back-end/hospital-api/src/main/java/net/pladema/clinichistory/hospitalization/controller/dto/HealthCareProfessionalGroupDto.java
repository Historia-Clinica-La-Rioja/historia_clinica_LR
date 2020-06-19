package net.pladema.clinichistory.hospitalization.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HealthCareProfessionalGroupDto {

    private Integer internmentEpisodeId;

    private Integer healthcareProfessionalId;

    private Boolean responsible;
}