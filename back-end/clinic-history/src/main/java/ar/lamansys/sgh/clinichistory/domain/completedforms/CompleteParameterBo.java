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
public class CompleteParameterBo {

	private Integer id;
	private Integer optionId;
	private Double numericValue;
	private String textValue;
	private String conceptPt;
	private String conceptSctid;

	public boolean isSnomed(){
	 return this.conceptPt != null && this.conceptSctid != null;
 	}

	 public boolean hasValues(){
		return optionId != null || numericValue != null || textValue != null || (isSnomed());
	 }

}
