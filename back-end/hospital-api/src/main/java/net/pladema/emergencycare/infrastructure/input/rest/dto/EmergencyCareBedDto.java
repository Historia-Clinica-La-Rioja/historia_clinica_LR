package net.pladema.emergencycare.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareBedDto {

	private Integer id;
	private String description;
	private boolean isAvailable;
	private String sectorDescription;
	private String roomDescription;
	private String bedDescription;
	private Boolean isBlocked;
}
