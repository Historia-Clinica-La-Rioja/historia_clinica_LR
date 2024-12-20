package net.pladema.emergencycare.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto.ShockroomDto;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareShockRoomDetailDto extends EmergencyCareAttentionPlaceDetailDto {

	private ShockroomDto shockroom;
}
