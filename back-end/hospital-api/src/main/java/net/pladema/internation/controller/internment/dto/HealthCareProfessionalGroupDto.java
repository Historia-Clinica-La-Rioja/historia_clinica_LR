package net.pladema.internation.controller.internment.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HealthCareProfessionalGroupDto {

    private Integer internmentEpisodeId;

    private Integer healthcareProfessionalId;

    private Boolean responsible;
}