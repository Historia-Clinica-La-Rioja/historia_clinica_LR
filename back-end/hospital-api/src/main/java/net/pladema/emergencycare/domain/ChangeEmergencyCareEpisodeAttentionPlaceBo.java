package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeEmergencyCareEpisodeAttentionPlaceBo {

	private Integer episodeId;
	private EmergencyCareEpisodeAttentionPlaceBo emergencyCareEpisodeAttentionPlace;
}
