package net.pladema.emergencycare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareEpisodeFilterBo {

	private List<Short> triageCategoryIds;

	private List<Short> typeIds;

	private Integer patientId;

	private String identificationNumber;

	private String patientFirstName;

	private String patientLastName;
	
	private Boolean mustBeEmergencyCareTemporal;

	private List<Integer> clinicalSpecialtySectorIds;

}
