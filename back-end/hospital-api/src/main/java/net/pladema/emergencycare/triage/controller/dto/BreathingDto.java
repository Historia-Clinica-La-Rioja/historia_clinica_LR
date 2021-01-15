package net.pladema.emergencycare.triage.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.pladema.clinichistory.documents.controller.dto.NewEffectiveClinicalObservationDto;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class BreathingDto  implements Serializable {

    private final Short respiratoryRetractionId;

    private final Boolean stridor;

    private final NewEffectiveClinicalObservationDto respiratoryRate;

    private final NewEffectiveClinicalObservationDto bloodOxygenSaturation;

}
