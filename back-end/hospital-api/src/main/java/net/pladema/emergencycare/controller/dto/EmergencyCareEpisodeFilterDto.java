package net.pladema.emergencycare.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmergencyCareEpisodeFilterDto {

	private List<Short> triageCategoryIds;

	private List<Short> typeIds;

	private Integer patientId;

	private String identificationNumber;

	private String patientFirstName;

	private String patientLastName;

	private boolean mustBeEmergencyCareTemporal;

	private List<Integer> clinicalSpecialtySectorIds;

}
