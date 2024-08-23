package net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ShockroomDto {

	private Integer id;
	private String description;
	private boolean isAvailable;

	public ShockroomDto(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
}
