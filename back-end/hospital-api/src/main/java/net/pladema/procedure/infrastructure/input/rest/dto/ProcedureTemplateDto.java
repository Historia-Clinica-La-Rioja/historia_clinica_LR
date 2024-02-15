package net.pladema.procedure.infrastructure.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureTemplateDto {

	private Integer id;
	private UUID uuid;
	private String description;
	private List<SnomedPracticeDto> associatedPractices;

}
