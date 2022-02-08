package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

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
public class RiskFactorObservationDto implements Serializable {

    private String loincCode;

    NewEffectiveClinicalObservationDto riskFactorObservation;

}
