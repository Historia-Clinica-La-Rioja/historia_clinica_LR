package net.pladema.clinichistory.outpatient.createoutpatient.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.ips.controller.dto.ClinicalObservationDto;

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
    @Nullable
    private ClinicalObservationDto height;

    @Valid
    @Nullable
    private ClinicalObservationDto weight;

    @Nullable
    private ClinicalObservationDto bmi;

}
