package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.controller.dto.BedDto;
import net.pladema.medicalconsultation.doctorsoffice.controller.dto.DoctorsOfficeDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto.ShockroomDto;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEmergencyCareDto extends EmergencyCareDto {

    Integer id;

    MasterDataDto emergencyCareState;

    DateTimeDto creationDate;

    DoctorsOfficeDto doctorsOffice;

	private ShockroomDto shockroom;

	private BedDto bed;
	
	private DateTimeDto endDate;

	private String institutionName;

	private Boolean canBeAbsent;

    public ResponseEmergencyCareDto(Integer id, String reason, MasterDataDto emergencyCareType,
                                    MasterDataDto entranceType, Boolean hasPoliceIntervention, PoliceInterventionDetailsDto policeIntervention,
                                    String ambulanceCompanyId, EmergencyCarePatientDto patient,
                                    MasterDataDto emergencyCareState, DateTimeDto createdOn){
        super(reason, emergencyCareType, entranceType, hasPoliceIntervention, policeIntervention, ambulanceCompanyId, patient);
        this.id = id;
        this.emergencyCareState = emergencyCareState;
        this.creationDate = createdOn;
    }

}
