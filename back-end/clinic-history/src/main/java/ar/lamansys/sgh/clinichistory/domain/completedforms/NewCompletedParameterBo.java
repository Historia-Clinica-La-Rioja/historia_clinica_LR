package ar.lamansys.sgh.clinichistory.domain.completedforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewCompletedParameterBo {

	private Integer parameterId;
	private Integer optionId;
	private Double numericValue;
	private String textValue;
	private Integer snomedId;

}
