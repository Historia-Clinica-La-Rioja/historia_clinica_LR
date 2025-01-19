package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PrescriptionProfessionDto {
	String profession;
	String snomedId;
}
