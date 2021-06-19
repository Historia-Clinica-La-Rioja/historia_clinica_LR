package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.NewEffectiveClinicalObservationDto;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class BreathingDto  implements Serializable {

    @Nullable
    private final Short respiratoryRetractionId;

    @Nullable
    private final Boolean stridor;

    @Nullable
    private final NewEffectiveClinicalObservationDto respiratoryRate;

    @Nullable
    private final NewEffectiveClinicalObservationDto bloodOxygenSaturation;

}
