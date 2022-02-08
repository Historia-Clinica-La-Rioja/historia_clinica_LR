package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationRiskFactor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RiskFactorObservationBo {

    private String loincCode;

    @Valid
    private ClinicalObservationBo riskFactor;

    public RiskFactorObservationBo(ObservationRiskFactor observationRiskFactor) {
        this.loincCode = observationRiskFactor.getLoincCode();
        this.riskFactor = new ClinicalObservationBo(observationRiskFactor.getId(),
                observationRiskFactor.getValue(),
                observationRiskFactor.getEffectiveTime());
    }

}
