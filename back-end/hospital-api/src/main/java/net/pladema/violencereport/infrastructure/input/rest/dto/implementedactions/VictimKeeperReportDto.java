package net.pladema.violencereport.infrastructure.input.rest.dto.implementedactions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.violencereport.domain.enums.EVictimKeeperReportPlace;

import javax.validation.constraints.NotNull;

import java.util.List;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class VictimKeeperReportDto {

	@NotNull(message = "{value.mandatory}")
	private Boolean werePreviousEpisodesWithVictimOrKeeper;

	private List<EVictimKeeperReportPlace> reportPlaces;

}
