package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public abstract class EmergencyCareDto implements Serializable {

    private List<SnomedDto> reasons;

    private MasterDataDto emergencyCareType;

    private MasterDataDto entranceType;

    private Boolean hasPoliceIntervention;

    private PoliceInterventionDetailsDto policeInterventionDetails;

    private String ambulanceCompanyId;

    private EmergencyCarePatientDto patient;
}
