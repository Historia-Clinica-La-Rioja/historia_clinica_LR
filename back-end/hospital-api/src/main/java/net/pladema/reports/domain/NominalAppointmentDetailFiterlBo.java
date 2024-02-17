package net.pladema.reports.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
public class NominalAppointmentDetailFiterlBo {

	private Integer institutionId;

	private LocalDate startDate;

	private LocalDate endDate;

	private Integer clinicalSpecialtyId;

	private Integer doctorId;

	private Integer hierarchicalUnitTypeId;

	private Integer hierarchicalUnitId;

	private Short appointmentStateId;

	private boolean includeHierarchicalUnitDescendants;

}
