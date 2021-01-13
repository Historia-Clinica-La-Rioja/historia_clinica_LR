package net.pladema.emergencycare.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class ResponseEmergencyCareDto extends EmergencyCareDto {

    Integer id;

    Short emergencyCareStateId;

    LocalDateTime createdOn;

    @Builder(builderMethodName = "responseAdministrativeBuilder")
    public ResponseEmergencyCareDto(Integer id, List<SnomedDto> reasons, Short typeId,
                                    Short entranceTypeId, PoliceInterventionDto policeIntervention, String ambulanceCompanyId, EmergencyCarePatientDto patient,
                                    Short emergencyCareStateId, LocalDateTime createdOn){
        super(reasons, typeId, entranceTypeId, policeIntervention, ambulanceCompanyId, patient);
        this.id = id;
        this.emergencyCareStateId = emergencyCareStateId;
        this.createdOn = createdOn;
    }

}
