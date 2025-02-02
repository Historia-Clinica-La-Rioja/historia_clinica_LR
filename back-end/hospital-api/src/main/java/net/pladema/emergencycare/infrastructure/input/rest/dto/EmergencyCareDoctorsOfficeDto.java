package net.pladema.emergencycare.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareDoctorsOfficeDto {

	private Integer id;
	private String description;
	private boolean isAvailable;
	private String sectorDescription;
	private Boolean isBlocked;
}
