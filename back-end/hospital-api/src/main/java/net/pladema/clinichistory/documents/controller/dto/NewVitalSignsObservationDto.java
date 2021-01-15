package net.pladema.clinichistory.documents.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class NewVitalSignsObservationDto implements Serializable {

    @Nullable
    private NewEffectiveClinicalObservationDto systolicBloodPressure;

    @Nullable
    private NewEffectiveClinicalObservationDto diastolicBloodPressure;

    @Nullable
    private NewEffectiveClinicalObservationDto temperature;

    @Nullable
    private NewEffectiveClinicalObservationDto heartRate;

    @Nullable
    private NewEffectiveClinicalObservationDto respiratoryRate;

    @Nullable
    private NewEffectiveClinicalObservationDto bloodOxygenSaturation;

}
