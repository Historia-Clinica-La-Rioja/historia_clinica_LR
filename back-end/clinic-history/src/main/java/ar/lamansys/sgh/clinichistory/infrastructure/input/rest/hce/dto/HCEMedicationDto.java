package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalTermDto;

@Getter
@Setter
@ToString
public class HCEMedicationDto extends ClinicalTermDto {

    private boolean suspended = false;
}