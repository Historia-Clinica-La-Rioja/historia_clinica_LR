package net.pladema.violencereport.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.infrastructure.input.rest.dto.aggressordetail.ViolenceReportAggressorDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.episodedetail.ViolenceEpisodeDetailDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions.ViolenceReportImplementedActionsDto;
import net.pladema.violencereport.infrastructure.input.rest.dto.victimdetail.ViolenceReportVictimDto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportDto {

	@Valid
	@NotNull(message = "{value.mandatory}")
	private ViolenceReportVictimDto victimData;

	@Valid
	@NotNull(message = "{value.mandatory}")
	private ViolenceEpisodeDetailDto episodeData;

	@NotEmpty
	@NotNull(message = "{value.mandatory}")
	private List<@Valid ViolenceReportAggressorDto> aggressorData;

	@Valid
	@NotNull(message = "{value.mandatory}")
	private ViolenceReportImplementedActionsDto implementedActions;

	private String observation;

}
