package net.pladema.medicalconsultation.shockroom.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ShockRoomBo {

	private Integer id;
	private String description;
	private boolean isAvailable;

	public ShockRoomBo(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
}
