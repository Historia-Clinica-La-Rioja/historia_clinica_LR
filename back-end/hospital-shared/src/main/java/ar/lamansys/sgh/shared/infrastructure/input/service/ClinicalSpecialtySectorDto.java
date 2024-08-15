package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalSpecialtySectorDto implements Serializable {

	private Integer id;
	private Integer sectorId;
	private Integer clinicalSpecialtyId;
	private String description;

}
