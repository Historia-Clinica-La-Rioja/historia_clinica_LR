package net.pladema.procedure.infrastructure.input.rest.dto;

import java.util.List;
import java.util.UUID;

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
public class ProcedureTemplateDto {

	private Integer id;
	private UUID uuid;
	private String description;
	private List<SnomedPracticeDto> associatedPractices;
	private Short statusId;

	public static ProcedureTemplateDto withoutPractices(Integer id, UUID uuid, String description, Short statusId) {
		return new ProcedureTemplateDto(id, uuid, description, null, statusId);
	}
}
