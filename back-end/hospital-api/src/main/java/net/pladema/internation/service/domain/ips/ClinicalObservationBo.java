package net.pladema.internation.service.domain.ips;

import lombok.*;
import net.pladema.internation.repository.ips.generalstate.VitalSignVo;

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

    private boolean deleted = false;

    public ClinicalObservationBo(VitalSignVo vitalSignVo) {
        super();
        this.id = vitalSignVo.getId();
        this.value = vitalSignVo.getValue();
    }

    public boolean mustSave() {
        return isDeleted() || isNew();
    }

    private boolean isNew() {
        return id == null;
    }

}
