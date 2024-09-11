package net.pladema.emergencycare.domain;

import lombok.Getter;
import lombok.Setter;
import net.pladema.establishment.domain.bed.EmergencyCareBedBo;

@Getter
@Setter
public class EmergencyCareBedDetailBo extends EmergencyCareAttentionPlaceDetailBo{

	private EmergencyCareBedBo bed;

	public EmergencyCareBedDetailBo(EmergencyCareBedBo bed){
		super();
		this.bed = bed;
	}
}
