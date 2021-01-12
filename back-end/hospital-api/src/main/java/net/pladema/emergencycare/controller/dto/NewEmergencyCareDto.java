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
public class NewEmergencyCareDto extends EmergencyCareDto {

    private final EmergencyCarePatientDto patient;

    private final String ambulanceCompanyId;

    @Builder(builderMethodName = "newAdministrativeBuilder")
    public NewEmergencyCareDto(EmergencyCarePatientDto patient, List<SnomedDto> reasons, Short typeId,
                               Short entranceTypeId, PoliceInterventionDto policeIntervention, String ambulanceCompanyId){
        super(reasons, typeId, entranceTypeId, policeIntervention);
        this.patient = patient;
        this.ambulanceCompanyId = ambulanceCompanyId;
    }
}
