package net.pladema.emergencycare.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyCareEpisodeFilterBo {

	private Short triageCategoryId;

	private Short typeId;

	private Integer patientId;

	private String identificationNumber;

	private String patientFirstName;

	private String patientLastName;

	private Boolean mustBeTemporal;

	private Boolean mustBeEmergencyCareTemporal;

}
