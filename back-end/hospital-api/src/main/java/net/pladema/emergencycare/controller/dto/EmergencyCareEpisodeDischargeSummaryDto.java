package net.pladema.emergencycare.controller.dto;

import ar.lamansys.sgx.shared.dates.controller.dto.DateTimeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCareEpisodeDischargeSummaryDto {

	private Integer episodeId;

	private DateTimeDto medicalDischargeOn;

	private DateTimeDto administrativeDischargeOn;

	private String medicalDischargeProfessionalName;

	private String medicalDischargeProfessionalLastName;

	private String observation;

}
