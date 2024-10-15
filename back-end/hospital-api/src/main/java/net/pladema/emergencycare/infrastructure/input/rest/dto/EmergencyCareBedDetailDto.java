package net.pladema.emergencycare.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareBedDetailDto extends EmergencyCareAttentionPlaceDetailDto {
	private EmergencyCareBedDto bed;

}
