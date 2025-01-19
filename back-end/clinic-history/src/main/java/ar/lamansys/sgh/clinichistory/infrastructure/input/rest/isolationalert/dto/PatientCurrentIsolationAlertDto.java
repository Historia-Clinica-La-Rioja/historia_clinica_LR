package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.isolationalert.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EIsolationStatus;
import ar.lamansys.sgh.clinichistory.domain.isolation.IsolationAlertAuthorBo;
import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.IsolationAlertDto;
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
	private IsolationAlertAuthorDto author;
	private MasterDataDto status;
	private IsolationAlertAuthorDto updatedBy;
	private DateTimeDto updatedOn;
	private Boolean isModified;
}
