package net.pladema.procedure.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SnomedPracticeDto {

	/**
	 * Note on the id type:
	 * The id may represent an sctid (long) or a real id from the snomed table (int).
	 * See the comments on BackofficeProcedureTemplateSnomedStore#save
	 */
	private Long id;
	private String sctid;
	private String pt;

}
