package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalTermDto;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class HCEAllergyDto extends ClinicalTermDto {

    private Short categoryId;

    @NotNull(message = "{value.mandatory}")
    private Short criticalityId;
}
