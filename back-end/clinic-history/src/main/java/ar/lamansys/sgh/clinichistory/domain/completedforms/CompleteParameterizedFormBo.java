package ar.lamansys.sgh.clinichistory.domain.completedforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompleteParameterizedFormBo {

	private Integer id;
	private List<CompleteParameterBo> parameters;

}
