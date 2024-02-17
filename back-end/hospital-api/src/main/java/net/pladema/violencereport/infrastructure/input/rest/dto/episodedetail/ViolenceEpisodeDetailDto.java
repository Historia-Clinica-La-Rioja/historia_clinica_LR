package net.pladema.violencereport.infrastructure.input.rest.dto.episodedetail;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto.SnomedDto;
import ar.lamansys.sgx.shared.dates.controller.dto.DateDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EViolenceEvaluationRiskLevel;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceEpisodeDetailDto {

	@NotNull(message = "{value.mandatory}")
	private DateDto episodeDate;

	@NotEmpty
	@NotNull(message = "{value.mandatory}")
	private List<@Valid SnomedDto> violenceTypeSnomedList;

	@NotEmpty
	@NotNull(message = "{value.mandatory}")
	private List<@Valid SnomedDto> violenceModalitySnomedList;

	private ViolenceTowardsUnderageDto violenceTowardsUnderage;

	@NotNull(message = "{value.mandatory}")
	private EViolenceEvaluationRiskLevel riskLevel;

}
