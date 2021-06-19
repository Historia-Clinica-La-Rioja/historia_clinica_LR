package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ar.lamansys.sgh.clinichistory.domain.ips.HealthConditionBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthConditionInfoDto implements Serializable {

    private Integer id;

    private SnomedDto snomed;

    public static HealthConditionInfoDto from(HealthConditionBo healthCondition) {
        if (healthCondition == null)
            return null;
        HealthConditionInfoDto result = new HealthConditionInfoDto();
        result.setId(healthCondition.getId());
        result.setSnomed(SnomedDto.from(healthCondition.getSnomed()));
        return result;
    }
}
