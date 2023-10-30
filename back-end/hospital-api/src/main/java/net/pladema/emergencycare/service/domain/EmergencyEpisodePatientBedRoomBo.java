package net.pladema.emergencycare.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyEpisodePatientBedRoomBo {

	private String bed;
	private String room;
}
