package net.pladema.medicalconsultation.shockroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ShockRoomBo {

	private Integer id;
	private String description;
	private boolean isAvailable;
	private String sectorDescription;

	public ShockRoomBo(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	public ShockRoomBo(Integer id, String description, boolean isAvailable) {
		this.id = id;
		this.description = description;
		this.isAvailable = isAvailable;
	}
}
