package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportImplementedActionsDto {

	private HealthCoordinationDto healthCoordination;

	@Valid
	@NotNull(message = "{value.mandatory}")
	private VictimKeeperReportDto victimKeeperReport;

	@Valid
	@NotNull(message = "{value.mandatory}")
	private InstitutionReportDto institutionReport;

	@Valid
	@NotNull(message = "{value.mandatory}")
	private SexualViolenceDto sexualViolence;

}
