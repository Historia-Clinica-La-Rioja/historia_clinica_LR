package net.pladema.emergencycare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCareSectorHasAttentionPlaceBo {

	private Boolean hasDoctorsOffices;
	private Boolean hasShockRooms;
	private Boolean hasBeds;
}
