package ar.lamansys.refcounterref.domain.referenceregulation;

import ar.lamansys.refcounterref.domain.enums.EReferenceRegulationState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ReferenceRegulationBo {

	private Integer id;
	private Integer referenceId;
	private Integer ruleId;
	private String ruleLevel;
	private EReferenceRegulationState state;
	private String reason;
	private String professionalName;
	private LocalDateTime createdOn;

}
