package net.pladema.procedure.domain;

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
@AllArgsConstructor
@NoArgsConstructor
public class ProcedureTemplateVo {

	private Integer id;
	private UUID uuid;
	private String description;
	private List<SnomedPracticeVo> associatedPractices;

}
