package net.pladema.emergencycare.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeEmergencyCareEpisodeAttentionPlaceDto {

	private Integer episodeId;
	private EmergencyCareEpisodeAttentionPlaceDto emergencyCareEpisodeAttentionPlace;
}
