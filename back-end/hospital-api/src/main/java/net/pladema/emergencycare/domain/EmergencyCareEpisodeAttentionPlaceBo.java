package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EmergencyCareEpisodeAttentionPlaceBo {

	private Integer doctorsOfficeId;
	private Integer shockroomId;
	private Integer bedId;
}
