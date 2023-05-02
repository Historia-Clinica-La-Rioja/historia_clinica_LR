package net.pladema.medicalconsultation.shockroom.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ShockroomVo {

	private Integer id;
	private String description;
}
