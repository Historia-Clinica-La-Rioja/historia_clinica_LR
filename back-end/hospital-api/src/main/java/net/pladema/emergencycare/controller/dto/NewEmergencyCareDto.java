package net.pladema.emergencycare.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.sgx.masterdata.dto.MasterDataDto;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class NewEmergencyCareDto extends EmergencyCareDto {

    @Builder(builderMethodName = "newAdministrativeBuilder")
    public NewEmergencyCareDto(List<SnomedDto> reasons, MasterDataDto emergencyCareType,
                               MasterDataDto entranceType, PoliceInterventionDto policeIntervention, String ambulanceCompanyId, EmergencyCarePatientDto patient){
        super(reasons, emergencyCareType, entranceType, policeIntervention, ambulanceCompanyId, patient);
    }
}
