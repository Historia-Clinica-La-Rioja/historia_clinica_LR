package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.sgx.dates.controller.dto.DateTimeDto;
import net.pladema.sgx.masterdata.dto.MasterDataDto;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEmergencyCareDto extends EmergencyCareDto {

    Integer id;

    MasterDataDto emergencyCareState;

    DateTimeDto creationDate;

    public ResponseEmergencyCareDto(Integer id, List<SnomedDto> reasons, MasterDataDto emergencyCareType,
                                    MasterDataDto entranceType, PoliceInterventionDto policeIntervention, String ambulanceCompanyId, PatientECEDto patient,
                                    MasterDataDto emergencyCareState, DateTimeDto createdOn){
        super(reasons, emergencyCareType, entranceType, policeIntervention, ambulanceCompanyId, patient);
        this.id = id;
        this.emergencyCareState = emergencyCareState;
        this.creationDate = createdOn;
    }

}
