package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public abstract class EmergencyCareDto implements Serializable {

    private final List<SnomedDto> reasons;

    private final Short typeId;

    private final Short entranceTypeId;

    private final PoliceInterventionDto policeIntervention;

    private final String ambulanceCompanyId;

    private final EmergencyCarePatientDto patient;
}
