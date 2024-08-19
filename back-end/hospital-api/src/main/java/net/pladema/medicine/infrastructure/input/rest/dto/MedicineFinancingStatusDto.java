package net.pladema.medicine.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MedicineFinancingStatusDto implements Serializable {

	@NotNull
	private Integer id;
	private String conceptSctid;
	private String conceptPt;
	@NotNull
	private Boolean financed;

}
