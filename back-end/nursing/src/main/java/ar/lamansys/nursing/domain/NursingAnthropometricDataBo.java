package ar.lamansys.nursing.domain;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.HeightDataValid;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.WeightDataValid;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalObservationDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.validation.Valid;

@Getter
@Setter
@ToString
public class NursingAnthropometricDataBo {

    @Valid
    @Nullable
    private ClinicalObservationDto bloodType;

    @Valid
    @HeightDataValid(message = "{diagnosis.anthropometric.height.invalid}")
    private ClinicalObservationDto height;

    @Valid
    @WeightDataValid(message = "{diagnosis.anthropometric.weight.invalid}")
    private ClinicalObservationDto weight;

    @Nullable
    private ClinicalObservationDto bmi;

	@Valid
	@Nullable
	private ClinicalObservationDto headCircumference;

}
