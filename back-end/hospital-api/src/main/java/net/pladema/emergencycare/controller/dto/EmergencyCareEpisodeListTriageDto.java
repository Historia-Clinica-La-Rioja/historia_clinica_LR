package net.pladema.emergencycare.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCareEpisodeListTriageDto {

	private Integer id;

	private String name;
	//colorCode
	private String color;
}
