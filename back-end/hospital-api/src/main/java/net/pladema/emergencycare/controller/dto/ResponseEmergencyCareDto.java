package net.pladema.emergencycare.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;

import java.util.List;

@Getter
@ToString
public class ResponseEmergencyCareDto extends EmergencyCareDto {

    Integer id;

    //TODO add create-date attribute

    @Builder(builderMethodName = "responseAdministrativeBuilder")
    public ResponseEmergencyCareDto(Integer id, List<SnomedDto> reasons, Short typeId,
                                    Short entranceTypeId, PoliceInterventionDto policeIntervention){
        super(reasons, typeId, entranceTypeId, policeIntervention);
        this.id = id;
    }
}
