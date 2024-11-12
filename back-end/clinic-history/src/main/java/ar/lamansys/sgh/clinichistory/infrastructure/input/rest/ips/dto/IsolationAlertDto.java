package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import java.util.List;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import ar.lamansys.sgx.shared.masterdata.infrastructure.input.rest.dto.MasterDataDto;

import com.fasterxml.jackson.annotation.JsonFormat;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;

@Getter
@Setter
public class IsolationAlertDto {
	private Integer id;
	private Integer healthConditionId;
 	private String healthConditionSctid;
	private String healthConditionPt;
 	private List<MasterDataDto> types;
 	private MasterDataDto criticality;
	private DateDto endDate;
	@Nullable
 	private String observations;
}
