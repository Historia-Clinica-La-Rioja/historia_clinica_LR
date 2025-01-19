package snomed.relations.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommercialMedicationSnomedBo {

	private String genericPt;

	private String genericSctid;

	private String commercialPt;

}
