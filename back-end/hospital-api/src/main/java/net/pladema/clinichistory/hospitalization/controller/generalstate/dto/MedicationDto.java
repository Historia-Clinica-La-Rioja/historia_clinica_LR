package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MedicationDto extends ClinicalTermDto {

    private String note;

    private boolean suspended = false;
}
