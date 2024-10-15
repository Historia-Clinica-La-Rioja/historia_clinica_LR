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
	private Boolean isBlocked;

	public ShockRoomBo(Integer id, String description, Boolean isBlocked) {
		this.id = id;
		this.description = description;
		this.isBlocked = isBlocked;
	}

	public ShockRoomBo(Integer id, String description, boolean isAvailable, Boolean isBlocked) {
		this.id = id;
		this.description = description;
		this.isAvailable = isAvailable;
		this.isBlocked = isBlocked;
	}

	public Boolean getIsBlocked(){
		return this.isBlocked == null ? false : this.isBlocked;
	}

	public boolean isAvailable() {
		return this.isAvailable && !this.getIsBlocked();
	}
}
