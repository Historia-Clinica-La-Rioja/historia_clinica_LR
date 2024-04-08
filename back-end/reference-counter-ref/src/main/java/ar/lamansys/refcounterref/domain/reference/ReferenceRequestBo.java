package ar.lamansys.refcounterref.domain.reference;

import ar.lamansys.refcounterref.domain.clinicalspecialty.ClinicalSpecialtyBo;
import ar.lamansys.refcounterref.domain.enums.EReferenceClosureType;
import ar.lamansys.refcounterref.domain.enums.EReferencePriority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ReferenceRequestBo {

	private Integer id;

	private List<ClinicalSpecialtyBo> clinicalSpecialties;

	private Integer careLineId;

	private String careLineDescription;

	private EReferencePriority priority;

	private String observation;

	private EReferenceClosureType closureType;

	private LocalDateTime closureDateTime;

	private Integer doctorId;

	public ReferenceRequestBo(Integer id, Integer careLineId, String careLineDescription,
							  Integer priorityId, String observation, Short closureTypeId,
							  LocalDateTime closureDateTime, Integer doctorId){
		this.id = id;
		this.careLineId = careLineId;
		this.careLineDescription = careLineDescription;
		this.priority = EReferencePriority.map(priorityId.shortValue());
		this.observation = observation;
		this.closureType = closureTypeId != null ? EReferenceClosureType.getById(closureTypeId) : null;
		this.closureDateTime = closureDateTime;
		this.doctorId = doctorId;
	}

}
