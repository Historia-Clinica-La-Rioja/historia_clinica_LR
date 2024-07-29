package ar.lamansys.sgh.publicapi.prescription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PrescriptionDosageBo {

	private Double unitDosis;

	private Double dayDosis;

	private Double duration;

	private String presentation;

	private Integer presentationQuantity;

	private Double quantity;

	private Integer frequency;

	private String frequencyUnit;
}
