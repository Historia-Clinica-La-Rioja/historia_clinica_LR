package net.pladema.emergencycare.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.annotation.Nullable;

@Getter
@ToString
@NoArgsConstructor(force = true)
public class NewEmergencyCareDto extends AEmergencyCareDto {

    @Nullable
    private Integer doctorsOfficeId;

    @Builder(builderMethodName = "newAdministrativeBuilder")
    public NewEmergencyCareDto(String reason, Short emergencyCareTypeId,
                               Short entranceTypeId, PoliceInterventionDetailsDto policeIntervention, Boolean hasPoliceIntervention,
                               String ambulanceCompanyId, AEmergencyCarePatientDto patient, Integer doctorsOfficeId){
        super(reason, emergencyCareTypeId, entranceTypeId, policeIntervention, hasPoliceIntervention, ambulanceCompanyId, patient);
        this.doctorsOfficeId = doctorsOfficeId;
    }
}
