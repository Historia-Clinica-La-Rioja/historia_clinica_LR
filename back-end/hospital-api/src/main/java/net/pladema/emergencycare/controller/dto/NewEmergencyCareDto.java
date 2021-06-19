package net.pladema.emergencycare.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class NewEmergencyCareDto extends AEmergencyCareDto {

    @Nullable
    private Integer doctorsOfficeId;

    @Builder(builderMethodName = "newAdministrativeBuilder")
    public NewEmergencyCareDto(List<SnomedDto> reasons, Short emergencyCareTypeId,
                               Short entranceTypeId, PoliceInterventionDetailsDto policeIntervention, Boolean hasPoliceIntervention,
                               String ambulanceCompanyId, AEmergencyCarePatientDto patient, Integer doctorsOfficeId){
        super(reasons, emergencyCareTypeId, entranceTypeId, policeIntervention, hasPoliceIntervention, ambulanceCompanyId, patient);
        this.doctorsOfficeId = doctorsOfficeId;
    }
}
