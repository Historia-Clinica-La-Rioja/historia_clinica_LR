package net.pladema.emergencycare.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.repository.domain.DischargeType;
import net.pladema.clinichistory.hospitalization.repository.domain.enums.EDischargeType;

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

	public DischargeTypeBo(EDischargeType dischargeType) {
		this.id = dischargeType.getId();
		this.description = dischargeType.getDescription();
	}

}
