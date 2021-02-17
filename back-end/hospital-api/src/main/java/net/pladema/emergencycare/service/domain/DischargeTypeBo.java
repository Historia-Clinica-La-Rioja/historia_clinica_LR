package net.pladema.emergencycare.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;

@Getter
@Setter
@ToString
public class DischargeTypeBo {

    private Short id;

    private String description;

    public DischargeTypeBo(DischargeType dischargeType) {
        this.id = dischargeType.getId();
        this.description = dischargeType.getDescription();
    }

}
