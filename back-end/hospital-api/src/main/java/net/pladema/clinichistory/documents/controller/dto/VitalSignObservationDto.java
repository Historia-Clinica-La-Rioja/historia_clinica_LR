package net.pladema.clinichistory.documents.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VitalSignObservationDto implements Serializable {

    private String loincCode;

    NewEffectiveClinicalObservationDto vitalSignObservation;

}
