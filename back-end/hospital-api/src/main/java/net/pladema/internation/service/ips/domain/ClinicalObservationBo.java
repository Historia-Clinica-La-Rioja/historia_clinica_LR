package net.pladema.internation.service.ips.domain;

import lombok.*;
import net.pladema.internation.repository.ips.generalstate.ClinicalObservationVo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalObservationBo implements Serializable {

    private Integer id;

    @NotNull
    private String value;

    public ClinicalObservationBo(ClinicalObservationVo clinicalObservationVo) {
        super();
        this.id = clinicalObservationVo.getId();
        this.value = clinicalObservationVo.getValue();
    }

}
