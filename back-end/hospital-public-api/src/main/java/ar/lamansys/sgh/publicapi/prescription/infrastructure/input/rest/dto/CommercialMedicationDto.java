package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommercialMedicationDto {
	String name;
	String snomedId;
}
