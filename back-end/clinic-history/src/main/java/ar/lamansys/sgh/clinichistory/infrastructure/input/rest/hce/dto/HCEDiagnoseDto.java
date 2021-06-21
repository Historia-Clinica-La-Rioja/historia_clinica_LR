package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalTermDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HCEDiagnoseDto extends ClinicalTermDto {

    private boolean main;

}
