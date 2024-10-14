package snomed.relations.cache.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommercialMedicationBo {

	private SnomedBo commercial;

	private SnomedBo generic;

	public CommercialMedicationBo(String commercialSctid, String commercialPt,
								  String genericSctid, String genericPt) {
		this.commercial = new SnomedBo(commercialSctid, commercialPt);
		this.generic = new SnomedBo(genericSctid, genericPt);
	}

}
