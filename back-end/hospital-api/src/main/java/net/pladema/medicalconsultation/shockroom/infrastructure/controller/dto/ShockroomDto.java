package net.pladema.medicalconsultation.shockroom.infrastructure.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ShockroomDto {

	private Integer id;
	private String description;
}
