package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuggestedCommercialMedicationDto {

	private String name;

	private String snomedId;

}
