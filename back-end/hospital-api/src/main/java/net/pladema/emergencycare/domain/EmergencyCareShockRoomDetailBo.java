package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.medicalconsultation.shockroom.domain.ShockRoomBo;

@Getter
@Setter
public class EmergencyCareShockRoomDetailBo extends EmergencyCareAttentionPlaceDetailBo{

	private ShockRoomBo shockroom;

	public EmergencyCareShockRoomDetailBo(ShockRoomBo shockroom){
		super();
		this.shockroom = shockroom;
	}
}
