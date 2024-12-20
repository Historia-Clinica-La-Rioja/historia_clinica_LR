package ar.lamansys.refcounterref.infraestructure.input.rest.dto.counterreference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@ToString
public class ReferenceAdministrativeClosureDto {

	@NotNull
	private Integer referenceId;

	@NotNull
	private String closureNote;

	private List<Integer> fileIds;

}
