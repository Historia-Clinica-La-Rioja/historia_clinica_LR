package snomed.relations.cache.infrastructure.input.rest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetCommercialMedicationSnomedDto {

	private String genericPt;

	private String genericSctid;

	private String commercialPt;

}
