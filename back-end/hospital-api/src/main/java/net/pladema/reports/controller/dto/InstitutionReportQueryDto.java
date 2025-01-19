package net.pladema.reports.controller.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.datastructures.PageRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstitutionReportQueryDto extends PageRequestDto {
	private String startDate;
	private String endDate;
	private Integer clinicalSpecialtyId;
	private Integer doctorId;
	private Integer hierarchicalUnitTypeId;
	private Integer hierarchicalUnitId;
	private boolean includeHierarchicalUnitDescendants;
	private Short appointmentStateId;
}
