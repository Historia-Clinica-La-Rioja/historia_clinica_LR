package net.pladema.reports.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddInstitutionReportToQueueDto {
	private DateDto startDate;
	private DateDto endDate;
	private Integer clinicalSpecialtyId;
	private Integer doctorId;
	private Integer hierarchicalUnitTypeId;
	private Integer hierarchicalUnitId;
	private boolean includeHierarchicalUnitDescendants;
	private Short appointmentStateId;
}
