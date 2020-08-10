package net.pladema.clinichistory.generalstate.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.ClinicalTermDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HCEDiagnoseDto extends ClinicalTermDto {

    private boolean main;

}
