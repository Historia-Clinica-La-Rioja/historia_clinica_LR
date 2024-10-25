package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto;

import java.util.List;

import javax.annotation.Nullable;

import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientCurrentIsolationAlertDto {
	private Integer isolationAlertId;
	private String healthConditionSctid;
	private String healthConditionPt;
	private List<MasterDataDto> types;
	private MasterDataDto criticality;
	private DateTimeDto startDate;
	private DateDto endDate;
	@Nullable
	private String observations;
	private ProfessionalInfoDto author;
	private MasterDataDto status;

}
