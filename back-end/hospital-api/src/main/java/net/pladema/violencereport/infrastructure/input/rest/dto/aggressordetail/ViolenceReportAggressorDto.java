package net.pladema.violencereport.infrastructure.input.rest.dto.aggressordetail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EAggressorRelationship;
import net.pladema.violencereport.domain.enums.ECriminalRecordStatus;
import net.pladema.violencereport.domain.enums.EViolenceFrequency;
import net.pladema.violencereport.domain.enums.ELiveTogetherStatus;
import net.pladema.violencereport.domain.enums.ERelationshipLength;
import net.pladema.violencereport.infrastructure.input.rest.dto.ViolenceReportActorDto;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportAggressorDto {

	@NotNull(message = "{value.mandatory}")
	private ViolenceReportActorDto<EAggressorRelationship> aggressorData;

	private Boolean hasGuns;

	private Boolean hasBeenTreated;

	private SecurityForceRelatedDto securityForceRelatedData;

	@NotNull(message = "{value.mandatory}")
	private ELiveTogetherStatus livesWithVictim;

	@NotNull(message = "{value.mandatory}")
	private ERelationshipLength relationshipLength;

	@NotNull(message = "{value.mandatory}")
	private EViolenceFrequency violenceViolenceFrequency;

	@NotNull(message = "{value.mandatory}")
	private ECriminalRecordStatus hasPreviousEpisodes;

}
