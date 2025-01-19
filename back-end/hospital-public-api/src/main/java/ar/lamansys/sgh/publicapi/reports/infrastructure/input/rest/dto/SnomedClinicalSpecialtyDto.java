package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class SnomedClinicalSpecialtyDto {
	private Integer id;
	private String description;
	private String snomedId;
}
