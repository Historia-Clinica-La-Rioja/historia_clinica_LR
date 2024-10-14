package ar.lamansys.sgh.clinichistory.domain.completedforms;

import ar.lamansys.sgh.shared.domain.forms.enums.EParameterType;
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
public class CompletedParameterSummaryBo {

	private Integer id;
	private String description;
	private Integer loincId;
	private EParameterType type;
	private String completedValue;

}
