package net.pladema.emergencycare.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareSectorHasAttentionPlaceDto {

	private Boolean hasDoctorsOffices;
	private Boolean hasShockRooms;
	private Boolean hasBeds;
}
