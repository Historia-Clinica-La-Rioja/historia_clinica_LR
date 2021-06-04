package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.controller.dto.ClinicalObservationDto;
import net.pladema.clinichistory.ips.controller.constraints.HeightDataValid;
import net.pladema.clinichistory.ips.controller.constraints.WeightDataValid;

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

}
