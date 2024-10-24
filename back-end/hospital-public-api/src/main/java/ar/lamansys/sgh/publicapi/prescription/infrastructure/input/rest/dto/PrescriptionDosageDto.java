package ar.lamansys.sgh.publicapi.prescription.infrastructure.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PrescriptionDosageDto {

	private Double unitDosis;

	private Double dayDosis;

	private Double duration;

	private String presentation;

	private Short presentationQuantity;

	private Double quantity;

	private Integer frequency;

	private Short presentationPackageQuantity;

	private String frequencyUnit;
}
