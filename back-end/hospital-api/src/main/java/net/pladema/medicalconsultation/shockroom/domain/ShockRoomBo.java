package net.pladema.medicalconsultation.shockroom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class ShockRoomBo {

	private Integer id;
	private String description;
}
