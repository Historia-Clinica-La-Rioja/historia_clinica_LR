package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.emergencycare.service.domain.HistoricEmergencyEpisodeBo;

@Setter
@Getter
@NoArgsConstructor
public class EmergencyCareEpisodeAttentionPlaceBo {

	private Integer doctorsOfficeId;
	private Integer shockroomId;
	private Integer bedId;

	public EmergencyCareEpisodeAttentionPlaceBo(HistoricEmergencyEpisodeBo hee){
		this.doctorsOfficeId = hee.getDoctorsOfficeId();
		this.shockroomId = hee.getShockroomId();
		this.bedId = hee.getBedId();
	}
}