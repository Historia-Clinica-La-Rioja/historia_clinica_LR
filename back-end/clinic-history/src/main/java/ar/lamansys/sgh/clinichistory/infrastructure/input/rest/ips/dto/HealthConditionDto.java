package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

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

	@Nullable
	private Boolean isAdded;
}
