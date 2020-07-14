package net.pladema.clinichistory.hospitalization.controller.generalstate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@Setter
@ToString
public class HealthConditionDto extends ClinicalTermDto {

    @Nullable
    private String verificationId;
}
