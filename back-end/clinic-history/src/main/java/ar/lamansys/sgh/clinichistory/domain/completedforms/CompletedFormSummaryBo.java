package ar.lamansys.sgh.clinichistory.domain.completedforms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompletedFormSummaryBo {

	private Integer id;
	private Integer formId;
	private Integer consultationId;
	private String formName;
	private List<CompletedParameterSummaryBo> parameters;

	public CompletedFormSummaryBo(Integer id, Integer formId, Integer consultationId, String formName){
		this.id = id;
		this.formId = formId;
		this.consultationId = consultationId;
		this.formName = formName;
	}

}
