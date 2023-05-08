package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AttentionPlacesQuantityDto {

	private Long shockroom;
	private Long doctorsOffice;
	private Long bed;
}
