package net.pladema.internation.controller.dto.ips;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@Setter
@ToString
public class HealthConditionDto extends ClinicalTermDto {

    private String verificationId;
}
