package net.pladema.establishment.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClinicalSpecialtySectorBo {

	private Integer id;
	private Integer sectorId;
	private Integer clinicalSpecialtyId;
	private String description;
}
