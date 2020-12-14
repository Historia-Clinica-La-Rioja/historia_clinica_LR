package net.pladema.clinichistory.requests.medicationrequests.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class HealthConditionInfoDto implements Serializable {

    private Integer id;

    private SnomedDto snomed;
}
