package net.pladema.internation.controller.internment.dto;

import lombok.Getter;
import lombok.Setter;
import net.pladema.renaper.controller.dto.PersonBasicDataResponseDto;

import javax.persistence.Column;

@Setter
@Getter
public class HealthCareProfessionalGroupDto {

    private Integer internmentEpisodeId;

    private Integer healthcareProfessionalId;

    private Boolean responsible;
}