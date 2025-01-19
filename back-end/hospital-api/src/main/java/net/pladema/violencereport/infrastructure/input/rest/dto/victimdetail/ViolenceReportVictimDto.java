package net.pladema.violencereport.infrastructure.input.rest.dto.victimdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EKeeperRelationship;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportActorDto;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportVictimDto {

	private Boolean canReadAndWrite;

	private ViolenceReportIncomeInformationDto incomeData;

	private Boolean hasSocialPlan;

	private ViolenceReportDisabilityDto disabilityData;

	private ViolenceReportInstitutionalizedDto institutionalizedData;

	@NotNull(message = "{value.mandatory}")
	private Boolean lackOfLegalCapacity;

	private ViolenceReportActorDto<EKeeperRelationship> keeperData;

}
