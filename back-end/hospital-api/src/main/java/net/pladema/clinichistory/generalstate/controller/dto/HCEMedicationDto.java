package net.pladema.clinichistory.generalstate.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.ClinicalTermDto;

@Getter
@Setter
@ToString
public class HCEMedicationDto extends ClinicalTermDto {

    private boolean suspended = false;
}