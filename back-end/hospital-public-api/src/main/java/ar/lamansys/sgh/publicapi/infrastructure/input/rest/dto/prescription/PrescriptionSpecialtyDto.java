package ar.lamansys.sgh.publicapi.infrastructure.input.rest.dto.prescription;

import lombok.Builder;

@Builder
public class PrescriptionSpecialtyDto {
	String specialty;
	String snomedId;
}
