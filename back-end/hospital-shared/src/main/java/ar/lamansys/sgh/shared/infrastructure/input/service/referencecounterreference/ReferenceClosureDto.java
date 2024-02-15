package ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ReferenceClosureDto {

	@NotNull
	private Integer referenceId;
	private Integer clinicalSpecialtyId;
	private String counterReferenceNote;
	@NotNull
	private Short closureTypeId;
	private List<Integer> fileIds;

}
