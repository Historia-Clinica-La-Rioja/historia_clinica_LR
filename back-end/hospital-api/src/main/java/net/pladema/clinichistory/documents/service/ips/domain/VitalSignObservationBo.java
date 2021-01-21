package net.pladema.clinichistory.documents.service.ips.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.documents.repository.ips.entity.ObservationVitalSign;

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
