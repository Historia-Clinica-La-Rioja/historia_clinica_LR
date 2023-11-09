package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.enums.EReferencePriority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceRequestBo {

	private Integer id;
	private Integer clinicalSpecialtyId;
	private String clinicalSpecialtyName;
	private Integer careLineId;
	private String careLineDescription;
	private EReferencePriority priority;
	private String observation;

	public ReferenceRequestBo(Integer id, Integer clinicalSpecialtyId, String clinicalSpecialtyName, Integer careLineId, String careLineDescription, Integer priorityId, String observation ){
		this.id = id;
		this.clinicalSpecialtyId = clinicalSpecialtyId;
		this.clinicalSpecialtyName = clinicalSpecialtyName;
		this.careLineId = careLineId;
		this.careLineDescription = careLineDescription;
		this.priority = EReferencePriority.map(priorityId.shortValue());
		this.observation = observation;
	}

}
