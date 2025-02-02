package ar.lamansys.sgh.publicapi.activities.infrastructure.input.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class BedRelocationInfoDto implements Serializable {

	private Integer bedId;
	private String bedNumber;
	private String bedCategory;
	private DateTimeDto relocationDate;
	private String careType;
	private ClinicalSpecialityDto service;
}
