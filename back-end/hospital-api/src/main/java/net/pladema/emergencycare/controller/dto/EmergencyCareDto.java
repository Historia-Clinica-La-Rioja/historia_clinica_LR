package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.clinichistory.hospitalization.controller.generalstate.dto.SnomedDto;
import net.pladema.sgx.masterdata.dto.MasterDataDto;

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

    private PoliceInterventionDetailsDto policeInterventionDetails;

    private String ambulanceCompanyId;

    private EmergencyCarePatientDto patient;
}
