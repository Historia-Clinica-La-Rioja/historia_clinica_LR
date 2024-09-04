package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public abstract class EmergencyCareDto implements Serializable {

    private String reason;

    private MasterDataDto emergencyCareType;

    private MasterDataDto entranceType;

    private Boolean hasPoliceIntervention;

    private PoliceInterventionDetailsDto policeInterventionDetails;

    private String ambulanceCompanyId;

    private EmergencyCarePatientDto patient;
}
