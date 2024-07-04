package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareEpisodeFilterDto {

	private Short triageCategoryId;

	private Short typeId;

	private Integer patientId;

	private String identificationNumber;

	private String patientFirstName;

	private String patientLastName;

	private boolean mustBeTemporal;

	private boolean mustBeEmergencyCareTemporal;

}
