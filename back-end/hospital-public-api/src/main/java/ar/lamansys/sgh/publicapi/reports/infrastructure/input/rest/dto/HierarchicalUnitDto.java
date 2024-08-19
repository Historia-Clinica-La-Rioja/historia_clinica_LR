package ar.lamansys.sgh.publicapi.reports.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HierarchicalUnitDto {
	private Integer id;
	private String description;
	private Short type;
}
