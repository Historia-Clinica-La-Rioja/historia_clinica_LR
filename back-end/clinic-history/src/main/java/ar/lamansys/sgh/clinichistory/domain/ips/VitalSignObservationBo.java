package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObservationVitalSign;
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
public class VitalSignObservationBo {

    private String loincCode;

    @Valid
    private ClinicalObservationBo vitalSign;

    public VitalSignObservationBo(ObservationVitalSign observationVitalSign) {
        this.loincCode = observationVitalSign.getLoincCode();
        this.vitalSign = new ClinicalObservationBo(observationVitalSign.getId(),
                observationVitalSign.getValue(),
                observationVitalSign.getEffectiveTime());
    }

}
