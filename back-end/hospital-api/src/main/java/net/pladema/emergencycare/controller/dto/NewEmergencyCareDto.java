package net.pladema.emergencycare.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class NewEmergencyCareDto extends AEmergencyCareDto {

    @Builder(builderMethodName = "newAdministrativeBuilder")
    public NewEmergencyCareDto(List<SnomedDto> reasons, Short emergencyCareTypeId,
                               Short entranceTypeId, PoliceInterventionDto policeIntervention, String ambulanceCompanyId, AEmergencyCarePatientDto patient){
        super(reasons, emergencyCareTypeId, entranceTypeId, policeIntervention, ambulanceCompanyId, patient);
    }
}
