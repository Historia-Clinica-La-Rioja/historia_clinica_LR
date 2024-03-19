package ar.lamansys.sgh.clinichistory.domain.anthropometricgraphic;

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
public class AnthropometricValueBo {

	private String weight;
	private String height;
	private String headCircumference;
	private String bmi;
	public boolean hasValues(){
		return (height != null ||
				weight != null ||
				headCircumference != null ||
				bmi != null);
	}

}
