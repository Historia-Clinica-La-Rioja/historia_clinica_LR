package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.HeadCircumferenceDataValid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.ClinicalObservationDto;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.HeightDataValid;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.constraints.WeightDataValid;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class OutpatientAnthropometricDataDto implements Serializable {

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
    @HeadCircumferenceDataValid(message = "{diagnosis.anthropometric.headCircumference.invalid}")
    private ClinicalObservationDto headCircumference;
}
